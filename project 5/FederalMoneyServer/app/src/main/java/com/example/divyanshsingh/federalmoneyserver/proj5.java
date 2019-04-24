package com.example.divyanshsingh.federalmoneyserver;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.divyanshsingh.federalmoneyserver.Iproj5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class proj5 extends Service {
    static proj5 sInstance;
    static int myInteger = 0;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    public static List mTitleArray;

    @Override
    public boolean onUnbind(Intent intent) {
        Message msg = MainActivity.mHandler.obtainMessage(1)  ;
        MainActivity.mHandler.sendMessage(msg);
        return super.onUnbind(intent);


    }

    @Override
    public void onCreate() {
        Message msg = MainActivity.mHandler.obtainMessage(2)  ;
        MainActivity.mHandler.sendMessage(msg);

        super.onCreate();
    }


    @Override
    public void onDestroy() {
        Message msg = MainActivity.mHandler.obtainMessage(3)  ;
        MainActivity.mHandler.sendMessage(msg);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Message msg = MainActivity.mHandler.obtainMessage(5)  ;
        MainActivity.mHandler.sendMessage(msg);
        return binder;
    }

    private final Iproj5.Stub binder = new Iproj5.Stub() {
        @Override
        public int getResult(int val1, int val2) throws RemoteException {
            return val1 * val2;
        }

        @Override
        public String getMessage(String name) throws RemoteException {
            return "Hello " + name + ", Result is:";
        }


        public List monthlyAvgCash(int year) throws RemoteException {
            Message msg = MainActivity.mHandler.obtainMessage(4)  ;
            MainActivity.mHandler.sendMessage(msg);
            String s = getJSON("http://api.treasury.io/cc7znvq/47d80ae900e04f2/sql/?q=SELECT%20%20%22open_mo%22%20FROM%20t1%20WHERE%20%22year%22="+year+"%20group%20by%20%22month%22", 5000);
            List x = Mcash(s);
            msg = MainActivity.mHandler.obtainMessage(5)  ;
            MainActivity.mHandler.sendMessage(msg);
            return x;
        }

        public List dailyCash(int day,int month, int year, int offset) throws RemoteException {
            Message msg = MainActivity.mHandler.obtainMessage(4)  ;
            MainActivity.mHandler.sendMessage(msg);
            String s = getJSON("http://api.treasury.io/cc7znvq/47d80ae900e04f2/sql/?q=SELECT%20%22open_today%22%20FROM%20t1%20WHERE%20%22date%22%20%3E%20%27"+year+"-"+month+"-"+day+"%27%20%20", 5000);
            List x = dcash(s,offset);
            msg = MainActivity.mHandler.obtainMessage(5)  ;
            MainActivity.mHandler.sendMessage(msg);
            return x;
        }

    };


    public String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public List Mcash(String s) {
        mTitleArray = new ArrayList<String>();
        try {
            JSONArray jsonArr = new JSONArray(s);
            JSONObject x = null;
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject c = jsonArr.getJSONObject(i);
                String open_today = c.getString("open_mo");
                mTitleArray.add("Amount " + open_today + " ");
                mTitleArray.add("\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mTitleArray;
    }




    public List dcash(String s,int offset) {
        mTitleArray = new ArrayList<String>();
        try {
            JSONArray jsonArr = new JSONArray(s);
            int  x = 0;
            for (int i = 0; i < 40; i++) {
                if(x<offset){
                    JSONObject c = jsonArr.getJSONObject(i);
                    int open_today = c.getInt("open_today");
                    if(open_today!=0){
                        mTitleArray.add(open_today);
                        mTitleArray.add("\n");
                        x++;
                        Log.i("open todayyy", String.valueOf(open_today));
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mTitleArray;
    }

}








