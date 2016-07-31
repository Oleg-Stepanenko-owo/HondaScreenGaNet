package com.ganet.catfish.hondascreenganet;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;

/**
 * Created by oleg on 31.07.2016.
 */
public class LogToFile  {
    final String FILENAME = "myLOG";
    private final MainActivity mActivity;

    public LogToFile( MainActivity mActivity ) {
        this.mActivity = mActivity;
    }

    void writeFile(String data) {
        try {
            // отрываем поток для записи
            String currTime = String.valueOf(System.currentTimeMillis()) + ":";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter( mActivity.openFileOutput(FILENAME, mActivity.MODE_APPEND)));
            //CharSequence ch = data.subSequence(0, data.length());
            bw.write( currTime + data );
            bw.close();
//            Log.d(LOG_TAG, "Файл записан");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
