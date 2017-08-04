package com.appnucleus.loginandregisteruser;

import android.*;
import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;

import static com.appnucleus.loginandregisteruser.R.string.co_chart;

public class NevigationDrawer extends AppCompatActivity{
    private static final int REQUEST_PERMISSION = 10;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    public static String prod_id,userName;
    ProgressDialog dialog1;
    String url1;
    RequestQueue rq;
    public static float temp2[], humid[], co[], ph[], light[];
    int aa;
    private Session session;
    public SharedPreferences someData;
    public static String filename = "MySharedString";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        session = new Session(this);

        prod_id = getIntent().getStringExtra("p_id1");
        userName = getIntent().getStringExtra("username1");

        //json object request start
        rq = Volley.newRequestQueue(this);

        //Shared prefrences start
        SharedPreferences wmbPreference = PreferenceManager.getDefaultSharedPreferences(this);
        someData = getSharedPreferences(filename,0);

        //sending push notification
        /* Intent inte = new Intent(NevigationDrawer.this, MyIntentService.class);
        startService(inte); */

        super.onCreate(savedInstanceState);

        boolean isFirstRun = wmbPreference.getBoolean("FIRSTRUN", true);
        if (isFirstRun)
        {
            // Code to run once
            SharedPreferences.Editor editor = wmbPreference.edit();
            editor.putBoolean("FIRSTRUN", false);
            editor.commit();
            firstuse();
        }

        sendr();

        setContentView(R.layout.activity_nevigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        dialog1 = ProgressDialog.show(NevigationDrawer.this, NevigationDrawer.this.getString(R.string.processing),
                NevigationDrawer.this.getString(R.string.loading), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void firstuse() {

        SharedPreferences.Editor editor = someData.edit();
        editor.putString("SharedString", prod_id);
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        int log_id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //permissions
            writetocsv();

        }
        if (log_id == R.id.log_out) {

            //code to clear the p_id from file after logout
            SharedPreferences.Editor editor = someData.edit();
            editor.clear();
            editor.commit();

            SharedPreferences wmbSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor1 = wmbSharedPreferences.edit();
            editor1.putBoolean("FIRSTRUN", true);
            editor1.commit();

            session.setLoggedin(false);

            Intent intent = new Intent(NevigationDrawer.this, Logout.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            //      return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    public void sendr() {
        someData = getSharedPreferences(filename, 0);
        final String dataReturned = someData.getString("SharedString","couldn't load data");

        url1 = "https://sens-agriculture.herokuapp.com/sensordata?pid=" + dataReturned;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray obj = response.getJSONArray("rows");
                    aa = obj.length();
                    temp2 = new float[aa];
                    humid = new float[aa];
                    co = new float[aa];
                    ph = new float[aa];
                    light = new float[aa];

                    // Toast.makeText(getApplicationContext(), " entered in method ", Toast.LENGTH_LONG).show();

                    for (int i = 0; i < aa; i++) {
                        JSONObject jsonObject1 = obj.getJSONObject(i);
                        temp2[i] = Float.parseFloat(jsonObject1.getString("temprature"));
                        humid[i] = Float.parseFloat(jsonObject1.getString("humidity"));
                        co[i] = Float.parseFloat(jsonObject1.getString("co_leve"));
                        ph[i] = Float.parseFloat(jsonObject1.getString("ph"));
                        light[i] = Float.parseFloat(jsonObject1.getString("light"));

                    }

                    navigationDrawing();
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

    public void writetocsv(){

        File exportDir = new File(Environment.getExternalStorageDirectory(), "Data-for-analysis");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "Data-from-server.csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            int i=0;
            while(i<aa)
            {
                String arrStr[] ={temp2[i]+"",humid[i]+"",co[i]+"",ph[i]+"", light[i]+""};
                csvWrite.writeNext(arrStr);
                i++;
            }
            csvWrite.close();


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void notif()
    {
        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify=new Notification.Builder(getApplicationContext()).setContentTitle("SenS").setContentText("").
                setContentTitle("Logged In as "+userName).setSmallIcon(R.drawable.abc).build();

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.notify(0, notify);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }


    public void navigationDrawing(){
        dialog1.dismiss();
        fragmentTransaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new TemperatureFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(R.string.temp_chart);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.temperature:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new TemperatureFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.temp_chart);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.humidity:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new HumidityFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.humidity_chart);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.moisture:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new MoistureFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.pH_chart);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.uv:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new UVFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.light_chart);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.co:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new COFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.co_chart);
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }

        });
    }

}
