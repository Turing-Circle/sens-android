package com.appnucleus.loginandregisteruser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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


                    Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                } else {


                    Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}