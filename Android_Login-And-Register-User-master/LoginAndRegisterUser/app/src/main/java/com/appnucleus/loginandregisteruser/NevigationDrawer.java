package com.appnucleus.loginandregisteruser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;

public class NevigationDrawer extends AppCompatActivity {
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nevigation_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        fragmentTransaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new TemperatureFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Temperature Chart");
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.temperature:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new TemperatureFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Temperature Chart");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.humidity:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new HumidityFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Humidity Chart");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.moisture:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new MoistureFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Moisture Chart");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.uv:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new UVFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("UV Chart");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.co:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new COFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("CO Chart");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
            return true;
        }
        if(log_id == R.id.log_out){
            Intent intent = new Intent(NevigationDrawer.this, Logout.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
      //      return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


}
