package com.appnucleus.loginandregisteruser;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class onoff_settings extends Fragment {

    public String status = "", number = "";
    Switch aSwitch;
    Button update;
    TextView phone_number;
    String filename = "MySharedString";
    SharedPreferences someData;
    ImageView imageView;

    public onoff_settings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_onoff_settings, container, false);
        someData = this.getActivity().getSharedPreferences(filename,0);
        final String dataReturned = someData.getString("pumpstatus","couldn't load data");
        final String dataReturned1 = someData.getString("number","couldn't load data");
        status = dataReturned;
        number = dataReturned1;

        aSwitch = (Switch) view.findViewById(R.id.switch1);
        update = (Button) view.findViewById(R.id.settings);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        phone_number = (TextView)view.findViewById(R.id.phone_text);

        phone_number.setText(number);

        if(status.equals("1")){
            aSwitch.setChecked(true);
            imageView.setImageResource(R.drawable.trans_off);
        }
        else{
            aSwitch.setChecked(false);
            imageView.setImageResource(R.drawable.trans_on);
        }

        update.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(" Update Phone Number");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        SharedPreferences.Editor editor = someData.edit();
                        editor.putString("number", m_Text);
                        editor.commit();
                        phone_number.setText(m_Text);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String a = someData.getString("number","couldn't load data");
                if(a.equals("0")){

                    Toast.makeText(getContext(), "No number specified for pump, specify a valid phone number", Toast.LENGTH_SHORT).show();
                    aSwitch.setChecked(false);
                }
                else
                {
                    if (isChecked == true) {
                        send("1",a);
                        Toast.makeText(getContext(), "Pump ON", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = someData.edit();
                        editor.putString("pumpstatus", "1");
                        imageView.setImageResource(R.drawable.trans_on);
                        ((TransitionDrawable) imageView.getDrawable()).startTransition(3000);
                        editor.commit();
                    } else {
                        send("0",a);
                        Toast.makeText(getContext(), "Pump OFF", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = someData.edit();
                        editor.putString("pumpstatus", "0");
                        imageView.setImageResource(R.drawable.trans_off);
                        ((TransitionDrawable) imageView.getDrawable()).startTransition(2000);
                        editor.commit();
                    }
                }
            }
        });
        return view;
    }

    public void send(String msgg, String number){

        //Getting intent and PendingIntent instance
        Intent intent=new Intent(getActivity(),onoff_settings.class);
        PendingIntent pi=PendingIntent.getActivity(getActivity(), 0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(number, null, msgg, pi,null);
    }
}