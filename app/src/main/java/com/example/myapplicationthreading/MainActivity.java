package com.example.myapplicationthreading;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.util.ServiceConfigurationError;

import static java.time.chrono.JapaneseEra.values;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean  isBound=false;
    private TextView txtNumber;
    private DisplayRandomTask asyncTask;
    private SampleService myService;
    private ServiceConnection connection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SampleService.LocalBinder binder=(SampleService.LocalBinder)service;
            myService=binder.getService();
            isBound=true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound=false;
        }
    };

//    private SecondAsyncTask asyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtNumber=findViewById(R.id.txtNumber);
        asyncTask=new DisplayRandomTask();
        asyncTask.execute(10);

//        asyncTask=new SecondAsyncTask();
//        asyncTask.execute(10);
//
//        AsyncTask asyncTask=new AsyncTask();
//        asyncTask.execute(10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=asyncTask){
            if(!asyncTask.isCancelled()){
                asyncTask.cancel(true);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, SampleService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound && null!=myService){
            unbindService(connection);
        }


    }

    private class DisplayRandomTask extends AsyncTask<Integer, Integer, Void>{
        @Override
        protected Void doInBackground(Integer... integers) {
            for(int i=0;i<integers[0];i++){
                if(isBound && null!=myService){
                    publishProgress(myService.getRandom());
                }
                SystemClock.sleep(1000);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            txtNumber.setText(String.valueOf(values[0]));
        }
    }

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(null!=asyncTask){
//            if(!asyncTask.isCancelled()){
//                asyncTask.cancel(true);
//            }
//        }
//    }

//    private class SecondAsyncTask extends android.os.AsyncTask<Integer, Integer, String>{
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            txtNumber=findViewById(R.id.txtNumber);
//        }
//
//        @Override
//        protected String doInBackground(Integer... integers) {
//            for(int i=0;i<integers[0];i++){
//                publishProgress(i);
//                SystemClock.sleep(1000);
//            }
//            return "Finished";
//        }

//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            txtNumber.setText(String.valueOf(values[0]));
//        }

//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            txtNumber.setText(s);
//
//        }
    }