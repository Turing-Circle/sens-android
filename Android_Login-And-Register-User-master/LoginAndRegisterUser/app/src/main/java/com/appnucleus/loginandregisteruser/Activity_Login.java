package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import android.os.Vibrator;


public class Activity_Login extends Activity {
    // LogCat tag
    private static final String TAG = Activity_Register.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister, btnLinkToForgotPassword;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    RequestQueue rq;
    ProgressDialog dialog;
    String p_id, name3, name4;
    int a, a1;
    String email, password, url;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        rq = Volley.newRequestQueue(this);
        super.onCreate(savedInstanceState);
        setContentView(com.appnucleus.loginandregisteruser.R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        btnLinkToForgotPassword = (Button) findViewById(R.id.forgot_password);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                try {
                    name3 = URLEncoder.encode(email, "UTF-8");
                    name4 = URLEncoder.encode(password, "UTF-8");
                    sendr();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Activity_Register.class);
                startActivity(i);
                finish();
                overridePendingTransition(com.appnucleus.loginandregisteruser.R.anim.push_left_in, com.appnucleus.loginandregisteruser.R.anim.push_left_out);
            }
        });

        btnLinkToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, ForgotPassword.class);
                startActivity(intent);

            }
        });
    }

    public void sendr() {
       // pDialog.setMessage("Logging in ...");
        //showDialog();
        dialog = ProgressDialog.show(Activity_Login.this, "", "Logging in....", true);
        url = "https://sens-agriculture.herokuapp.com/userdata?uname="+name3+"&pwd="+name4;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray obj = response.getJSONArray("rows");
                    a1 = obj.length();
                    if(a1>0) {
                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject jsonObject1 = obj.getJSONObject(i);
                            p_id = jsonObject1.getString("product_id");
                            a = p_id.length();
                            check();
                        }
                    } else{
                        dialog.dismiss();
                        Animation shake = AnimationUtils.loadAnimation(Activity_Login.this, R.anim.shake);
                        inputPassword.startAnimation(shake);
                        inputEmail.startAnimation(shake);
                        vibrate(btnLogin);
                        Toast.makeText(getApplicationContext(), "E-mail/password Invalid", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();
            }
        });

        rq.add(jsonObjectRequest);
    }

    public void check() {

        dialog.dismiss();

        if (a == 5) {
            Intent i1 = new Intent(Activity_Login.this, NevigationDrawer.class);
            i1.putExtra("p_id1", p_id);
            startActivity(i1);
            finish();

            inputEmail.setText("");
            inputPassword.setText("");

        }
    }

    public void vibrate(View view) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}