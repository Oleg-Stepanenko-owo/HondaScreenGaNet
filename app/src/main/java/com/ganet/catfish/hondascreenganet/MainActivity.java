package com.ganet.catfish.hondascreenganet;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ganet.catfish.hondascreenganet.Data.ActiveTrack;
import com.ganet.catfish.hondascreenganet.Data.Track;

import java.lang.ref.WeakReference;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GaNetManager mGANET;
    private ReadFromFile readFileObj;
    private TextView timeTextView;
    private Button mStartBtn;
    private TextView tracksList, tvAction, tvVol, tvPlayTrack, vtComLog;

    private MyHandler mHandler;
    private UsbCom usbService;
    private LogToFile mFileLog;

    //--------------------- USB SERIAL ------------------------------------------------------
    /*
    * Notifications from UsbService will be received here.
    */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbCom.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbCom.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbCom.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbCom.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbCom.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbCom.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbCom.ACTION_NO_USB);
        filter.addAction(UsbCom.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbCom.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbCom.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        static int iPK = 0;
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbCom.MESSAGE_FROM_SERIAL_PORT:
                    final String data = (String) msg.obj;
                    // mActivity.get().vtComLog.append(String.valueOf(iPK++) + ":" + data);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mActivity.get().mFileLog.writeFile( data );
                            mActivity.get().mGANET.mParser.parseLine(data);
                        }
                    }).start();

                    break;
                case UsbCom.CTS_CHANGE:
                    Toast.makeText(mActivity.get(), "CTS_CHANGE",Toast.LENGTH_LONG).show();
                    break;
                case UsbCom.DSR_CHANGE:
                    Toast.makeText(mActivity.get(), "DSR_CHANGE",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbCom.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbCom.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    //---------------------------------------------- main functionality ----------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeTextView = (TextView) findViewById( R.id.textTime );
        mStartBtn = (Button) findViewById( R.id.button1);
        tracksList = (TextView) findViewById(R.id.trackView);
        tvAction = (TextView) findViewById(R.id.tvAction);
        tvVol = (TextView) findViewById(R.id.tvVol);
        tvPlayTrack = (TextView) findViewById(R.id.tvPlayTrack);
        vtComLog = (TextView) findViewById(R.id.tvComLog);

        mFileLog = new LogToFile(this);
        mHandler = new MyHandler(this);

        mGANET = new GaNetManager( this );
        readFileObj = new ReadFromFile("/data/data/com.ganet.catfish.hondascreenganet/log/Mylog.txt", mGANET );
        mStartBtn.setOnClickListener( this );

        setFilters();  // Start listening notifications from UsbService
        startService(UsbCom.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mUsbReceiver);
        unbindService(usbConnection);
    }

    /**
     *
     * @param updateAction
     */
    public void invalidate (ParserGANET.eParse updateAction ) {
        switch ( updateAction ) {
            case eActiveTr:
                updateActiveTrackView( mGANET.mActiveTrack );
                break;
            case eTr:
                updateTrackView( new TreeMap<Integer, Track>(mGANET.mTrack) );
                break;
            case eFolder:
                break;
            case eTime:
                updateTimeUi(mGANET.mDevTime.getTime());
                break;
            case eEjectDisk:
                mGANET.currentDiskId = 0;
                mGANET.currentLevel = 0;
                updataDiskUi( mGANET.currentDiskId );
                break;
            case eInsertDisk:
                updataDiskUi( mGANET.currentDiskId );
                break;
            case eVolume:
                updateVolUi( mGANET.mVol.getVol() );
                break;
            case eNone:
                break;
        }
    }

    private void updataDiskUi(final int diskID ) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              TextView tvDisk = (TextView) findViewById( R.id.tvTrackDisk );
                              TextView tvTr = (TextView) findViewById( R.id.tvTrackId );
                              TextView tvTime = (TextView) findViewById( R.id.tvTrackTime );

                              //Eject
                              if( diskID == 0 ){
                                  tracksList.setText("");
                                  tvDisk.setText( "" );
                                  tvTr.setText( "" );
                                  tvTime.setText( R.string.defaultTime );
                                  tvAction.setText("EJECT");
                              } else { //Insert disk
                                  tvAction.setText("ISERT");
                                  TextView tvDisk1 = (TextView) findViewById( R.id.tvTrackDisk );
                                  tvDisk.setText( String.valueOf(diskID) );
                              }
                          }
                      }
        );
    }

    @Override
    public void onClick(View view) {
        readFileObj.startRead( mGANET.getParser() );
    }

    public void updateTimeUi( final String timeUI ) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              timeTextView.setText( timeUI );
                          }
                      }
        );
    }

    public void updateVolUi( final int iVol ) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              tvVol.setText( String.valueOf(iVol) );
                          }
                      }
        );
    }

    public void updateTrackView( final Map<Integer, Track> tracks ) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              String retTracks = "";
                              try{
                                  for (Map.Entry<Integer, Track> e : tracks.entrySet()) {
                                      if( e.getValue().isReadyToShow() ) {
                                          retTracks += e.getValue().getTrackId() + ":" + e.getValue().getName() + "\r\n";
//                                  System.out.println(e.getKey() + ": " + e.getValue());
                                      }
                                  }
                                  tracksList.setText(retTracks);
                              } catch (ConcurrentModificationException m){
                                  System.out.println("ERROR: " + m.getMessage() );
                              }
                          }
                      }
        );
    }

    public void updateActiveTrackView( final ActiveTrack activeTrack ) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              TextView tvDisk = (TextView) findViewById( R.id.tvTrackDisk );
                              TextView tvTr = (TextView) findViewById( R.id.tvTrackId );
                              TextView tvTime = (TextView) findViewById( R.id.tvTrackTime );

                              tvDisk.setText( String.valueOf(activeTrack.diskID) );
                              tvTr.setText( String.valueOf(activeTrack.trackId) );
                              tvTime.setText( String.valueOf(activeTrack.playMin) + ":" + String.valueOf(activeTrack.playSec) );
                              tvAction.setText("PLAY");

                              tvPlayTrack.setText( mGANET.getTrackById(activeTrack.trackId) );
                          }
                      }
        );
    }
}
