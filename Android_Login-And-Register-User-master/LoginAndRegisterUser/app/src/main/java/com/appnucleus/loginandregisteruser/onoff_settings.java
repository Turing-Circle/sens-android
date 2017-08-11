package com.appnucleus.loginandregisteruser;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

public class onoff_settings extends Fragment {

    public String status = "";
    ImageView imageView;
    Switch aSwitch;
    boolean turnedOn = false;
    String filename = "MySharedString";
    SharedPreferences someData;


    public onoff_settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onoff_settings, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageView);
        aSwitch = (Switch) view.findViewById(R.id.switch1);

        someData = this.getActivity().getSharedPreferences(filename, 0);
        final String dataReturned = someData.getString("pumpstatus", "couldn't load data");
        status = dataReturned;

        if (status.equals("1")) {
            aSwitch.setChecked(true);
            imageView.setImageResource(R.drawable.trans_on);
        } else {
            aSwitch.setChecked(false);
            imageView.setImageResource(R.drawable.trans_off);
        }


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    send("1");

                    Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor editor = someData.edit();
                    editor.putString("pumpstatus", "1");

                    ((TransitionDrawable) imageView.getDrawable()).startTransition(3000);

                    editor.commit();
                } else {
                    send("0");
                    Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = someData.edit();
                    editor.putString("pumpstatus", "0");
                    ((TransitionDrawable) imageView.getDrawable()).startTransition(3000);

                    editor.commit();

                }

            }
        });
        return view;
    }


    public void send(String msgg){

        String no="9891154325";

        //Getting intent and PendingIntent instance
        Intent intent=new Intent(getActivity(),onoff_settings.class);
        PendingIntent pi=PendingIntent.getActivity(getActivity(), 0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(no, null, msgg, pi,null);

        Toast.makeText(getActivity(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }


}