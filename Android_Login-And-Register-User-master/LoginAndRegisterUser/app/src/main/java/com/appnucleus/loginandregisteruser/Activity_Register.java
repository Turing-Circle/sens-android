package com.appnucleus.loginandregisteruser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static android.content.ContentValues.TAG;

public class Activity_Register extends Activity {

    EditText name,email,phone,location,pass_a,productid;
    String namex,emailx,phonex,locationx,passx,prodct,url,pass_c;
    RequestQueue rq;
    ProgressDialog dialog;
    Button b;
    CheckBox terms_conditions;
    EditText confirmPassword;
    AutoCompleteTextView autoCompleteTextView;
    String[] location_names;
    private Button btnLinkToLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rq = Volley.newRequestQueue(this);

        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        phone = (EditText)findViewById(R.id.mobileNumber);
        location = (EditText)findViewById(R.id.location);
        pass_a = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        productid = (EditText)findViewById(R.id.p_id);
        terms_conditions = (CheckBox) findViewById(R.id.terms_conditions);
        b = (Button)findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);
            terms_conditions.setTextColor(csl);
        } catch (Exception e) {
        }


        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                namex = name.getText().toString().trim();
                emailx = email.getText().toString().trim();
                phonex = phone.getText().toString().trim();
                locationx = location.getText().toString().trim();
                passx = pass_a.getText().toString().trim();
                prodct = productid.getText().toString().trim();
                pass_c = confirmPassword.getText().toString().trim();

                if(namex.equals("")||emailx.equals("")||phonex.equals("")||passx.equals("")||locationx.equals("")||prodct.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.details, Toast.LENGTH_SHORT).show();
                    vibrate(btnRegister);
                }
                else {
                    if(!terms_conditions.isChecked()) {
                        Toast.makeText(getApplicationContext(), R.string.terms, Toast.LENGTH_SHORT).show();
                        vibrate(btnRegister);
                    }
                    else {
                        if(passx.equals(pass_c)) {
                            sendd();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), R.string.passNmatch, Toast.LENGTH_SHORT).show();
                            vibrate(btnRegister);
                        }
                    }
                }
            }
        });

                // Link to Login Screen
                btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), Activity_Login.class);
                        startActivity(i);
                        overridePendingTransition(com.appnucleus.loginandregisteruser.R.anim.push_left_in, com.appnucleus.loginandregisteruser.R.anim.push_left_out);
                    }
                });

                // Auto Complete text field
                autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.location);
                location_names = getResources().getStringArray(R.array.location);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, location_names);
                autoCompleteTextView.setAdapter(adapter);
            }

            public void sendd() {
                dialog = new ProgressDialog(Activity_Register.this);
                dialog.setMessage(Activity_Register.this.getString(R.string.signing));
                dialog.show();

                url = "https://sens-agriculture.herokuapp.com/register?name=" + namex + "&uname=" + emailx + "&phone=" + phonex + "&loc=" + locationx + "&pwd=" + passx + "&pid=" + prodct;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            check();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Registration Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                Activity_Register.this.getString(R.string.signError), Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                rq.add(jsonObjectRequest);
            }

    public void vibrate(View view) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

            public void check() {
                dialog.dismiss();

                Toast.makeText(getApplicationContext(), R.string.user_registered, Toast.LENGTH_LONG).show();

                name.setText("");
                email.setText("");
                phone.setText("");
                location.setText("");
                pass_a.setText("");
                productid.setText("");
                confirmPassword.setText("");

                Intent i1 = new Intent(Activity_Register.this, Activity_Login.class);
                startActivity(i1);
            }
        }