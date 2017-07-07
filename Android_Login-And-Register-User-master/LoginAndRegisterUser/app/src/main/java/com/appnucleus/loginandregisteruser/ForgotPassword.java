package com.appnucleus.loginandregisteruser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private static View view;
    EditText inputEmail;
    Button submit;
    ProgressDialog dialog;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        inputEmail = (EditText) findViewById(R.id.registered_emailid);
        submit = (Button) findViewById(R.id.forgot_button);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString();

                if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter your Registered Email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Sending", Toast.LENGTH_SHORT).show();
                    //dialog =  ProgressDialog.show(ForgotPassword.this, "", "Sending..", true);
                    waite();
                }
            }
        });

}

        public void waite() {
            try {
                TimeUnit.SECONDS.sleep(2);

                Intent i = new Intent(getApplicationContext(), Activity_Login.class);
                startActivity(i);
                finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

         // dialog.dismiss();
        inputEmail.setText("");

    }

}
