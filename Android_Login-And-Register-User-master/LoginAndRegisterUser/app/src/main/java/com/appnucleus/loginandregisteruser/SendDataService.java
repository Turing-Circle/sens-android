package com.appnucleus.loginandregisteruser;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by roysh on 8/4/2017.
 */

public class SendDataService extends Service {

    SharedPreferences someData;
    String filename = "MySharedString";
    String url1;
    RequestQueue rq;
    private final LocalBinder mBinder = new LocalBinder();
    int aa;
    public String p_id;
    static float temprature_sum;


    public class LocalBinder extends Binder {
        public SendDataService getService() {
            return SendDataService .this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        rq = Volley.newRequestQueue(SendDataService.this);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                    someData = getSharedPreferences(filename,0);
                    final String dataReturned = someData.getString("SharedString","couldn't load data");
                    p_id = dataReturned;
                    if(dataReturned.length() == 5){
                        sendr();
                    }

                }
            },0,10000);



        return android.app.Service.START_STICKY;
    }

    public void notif()
    {
        temprature_sum = temprature_sum/5;
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder(getApplicationContext()).setContentTitle("SenS").setContentText("").
                setContentTitle("Average temprature : "+temprature_sum).setSmallIcon(R.drawable.abc).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        temprature_sum = 0;
    }


    public void sendr() {

        url1 = "https://sens-agriculture.herokuapp.com/sensordata?pid=" + p_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray obj = response.getJSONArray("rows");
                    aa = obj.length();

                        for (int i = aa-1; i > aa - 7; i--) {
                            JSONObject jsonObject1 = obj.getJSONObject(i);
                            temprature_sum += Float.parseFloat(jsonObject1.getString("temprature"));
                        }

                        notif();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        rq.add(jsonObjectRequest);
    }
}