package com.appnucleus.loginandregisteruser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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


public class ForgotPassword extends AppCompatActivity {

    private EditText editTextEmail;
    private Button buttonSend;
    RequestQueue rq;
    String url,email,email_server;
    AlertDialog.Builder alertd_;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        rq = Volley.newRequestQueue(ForgotPassword.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        alertd_ = new AlertDialog.Builder(ForgotPassword.this);

        editTextEmail = (EditText) findViewById(R.id.registered_emailid);
        buttonSend = (Button) findViewById(R.id.forgot_button);

        buttonSend.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = editTextEmail.getText().toString().trim();
                sendr();
                pDialog = ProgressDialog.show(ForgotPassword.this, ForgotPassword.this.getString(R.string.processing),
                        ForgotPassword.this.getString(R.string.checking), true);
            }
        });

    }

    public void sendr() {

        url = "https://sens-agriculture.herokuapp.com/check?uname="+email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray obj = response.getJSONArray("rows");
                    int a1 = obj.length();
                    if(a1>0) {
                        JSONObject jsonObject1 = obj.getJSONObject(0);
                        email_server = jsonObject1.getString("email");
                        check();
                    }
                    else{
                        alertd_.setTitle("");
                        displayAlert("Enter correct user id");
                        pDialog.dismiss();
                    }

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


    private void sendEmail() {
        //Getting content for email
        String email = editTextEmail.getText().toString().trim();
        String newEmail = "";

        for(int i = 0;i < email.length();i++){
            if(i%2 == 0){
                int asciiVal = (int)email.charAt(i);
                int newAsciiVal = asciiVal-1;
                char ch = (char)newAsciiVal;
                newEmail+=ch;
            }else{
                int asciiVal = (int)email.charAt(i);
                int newAsciiVal = asciiVal+1;
                char ch = (char)newAsciiVal;
                newEmail+=ch;
            }
        }

        String subject = "Reset Password - SenS";
        String message = "Link to reset your password : \n" +
                "https://sens-agriculture.herokuapp.com/forgotPassword?uname="+newEmail +"\n\n"+
                "Please ignore if you have not requested one \n\n\n" +
                "Thank You, \n" +
                "Team Sens";

        SendMail sm = new SendMail(this, email, subject, message);
        sm.execute();
    }

    public void check(){
        if(email.equals(email_server)){
            sendEmail();
            pDialog.dismiss();
        }
    }

    public void displayAlert(String message)
    {
        alertd_.setMessage(message);
        alertd_.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                editTextEmail.setText("");
            }
        });

        AlertDialog alertDialog = alertd_.create();
        alertDialog.show();
    }
}