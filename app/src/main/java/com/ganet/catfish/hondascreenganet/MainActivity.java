package com.ganet.catfish.hondascreenganet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GaNetManager mGANET;
    private ReadFromFile readFileObj;
    private TextView timeTextView;
    private Button mStartBtn;
    private TextView tracksList, tvAction, tvVol, tvPlayTrack;

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



        mGANET = new GaNetManager( this );
        readFileObj = new ReadFromFile("/data/data/com.ganet.catfish.hondascreenganet/log/yam_fm1.txt", mGANET );
        mStartBtn.setOnClickListener( this );
    }

    public void invalidate ( ParserGANET.eParse updateAction ) {
        switch ( updateAction ){
            case eActiveTr:
                updateActiveTrackView( mGANET.mActiveTrack );
                break;
            case eTr:
                updateTrackView( mGANET.mTrack );
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

    public void updateTrackView(final Map<Integer, Track> tracks ) {
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              String retTracks = "";
                              for (Map.Entry<Integer, Track> e : tracks.entrySet()) {
                                  if( e.getValue().isReadyToShow() ) {
                                      retTracks += e.getValue().getTrackId() + ":" + e.getValue().getName() + "\r\n";
//                                  System.out.println(e.getKey() + ": " + e.getValue());
                                  }
                              }
                              tracksList.setText(retTracks);
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
