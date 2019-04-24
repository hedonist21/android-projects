package com.example.divyanshsingh.federalmoneyserver;

import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.divyanshsingh.federalmoneyserver.Iproj5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    public static TextView tv;
    public static Runnable runnable;
    public static Runnable x;
    public static String status = "Service started but not bound";
    public static boolean created = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.status);
        tv.setText(status);
        created = true;
    }


    public static android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            int what = msg.what;

            switch (what) {
                case 1:
                    status = " service is unbinding";
                    if (created)
                        tv.setText(status);
                    break;
                case 2:
                    status = " service is created ";
                    if (created == true)
                        tv.setText(status);

                    break;
                case 3:
                    status = " service is destroyed";
                        if (created == true)
                            tv.setText(status);
                    break;
                case 4:
                    status = " service is running api";
                    if (created == true)
                        tv.setText(status);

                    break;
                case 5:
                        status = " service is bound but idle";
                        if (created == true)
                            tv.setText(status);
                    break;

            }
        }
    };

}

