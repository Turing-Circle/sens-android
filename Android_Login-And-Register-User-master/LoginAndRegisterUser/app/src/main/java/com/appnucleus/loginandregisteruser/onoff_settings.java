package com.appnucleus.loginandregisteruser;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class onoff_settings extends Fragment {

    Switch aSwitch;

    public onoff_settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onoff_settings, container, false);

        aSwitch = (Switch) view.findViewById(R.id.switch1);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked == true) {
                    send("1");
                    Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                } else {
                    send("0");
                    Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
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