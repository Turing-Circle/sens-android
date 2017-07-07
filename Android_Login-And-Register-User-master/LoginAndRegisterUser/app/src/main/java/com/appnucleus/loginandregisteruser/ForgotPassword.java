package com.appnucleus.loginandregisteruser;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    //Declaring EditText
    private EditText editTextEmail;

    //Send button
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initializing the views
        editTextEmail = (EditText) findViewById(R.id.registered_emailid);


        buttonSend = (Button) findViewById(R.id.forgot_button);

        //Adding click listener
        buttonSend.setOnClickListener(this);
    }


    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String subject = "Reset Password - SenS";
        String message = "Link to reset your password : \n" +
                "https://http://sens-agriculture.herokuapp.com/forgotPassword?uname="+email +"\n\n"+
                "Please ignore if you have not requested one \n\n\n" +
                "Thank You, \n" +
                "Team Sens";

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    @Override
    public void onClick(View v) {
        sendEmail();
    }
}