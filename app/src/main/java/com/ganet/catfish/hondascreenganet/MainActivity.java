package com.ganet.catfish.hondascreenganet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GaNetManager mGANET;
    private ReadFromFile readFileObj;
    private TextView timeTextView;
    private Button mStartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timeTextView = (TextView) findViewById( R.id.textTime );
        mStartBtn = (Button) findViewById( R.id.button1);


        mGANET = new GaNetManager( this );
        readFileObj = new ReadFromFile("/data/data/com.ganet.catfish.hondascreenganet/log/yamGANET1.log", mGANET );
        mStartBtn.setOnClickListener( this );
    }

    public void invalidate ( ParserGANET.eParse updateAction ) {
        switch ( updateAction ){
            case eActiveTr:
                break;
            case eTr:
                break;
            case eFolder:
                break;
            case eTime:
                updateTimeUi(mGANET.getParser().getDevTime().getTime());
                break;
            case eNone:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        readFileObj.startRead( mGANET.getParser() );
        // timeTextView.setText( "1244" );
    }

    public void updateTimeUi( final String timeUI ){
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              timeTextView.setText( timeUI );
                          }
                      }
        );
    }
}
