package com.example.myapplicationthreading;

import android.os.SystemClock;
import android.util.Log;

public class AsyncTask extends android.os.AsyncTask<Integer, Void, Void> {
    private static final String TAG = "AsyncTask";

    @Override
    protected Void doInBackground(Integer... integers) {
        for(int i=0;i<integers[0];i++){
            Log.d(TAG, "doInBackground: i was "+i);
            SystemClock.sleep(1000);
        }

        return null;
    }
}
