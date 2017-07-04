package com.appnucleus.loginandregisteruser;

import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;

import java.util.regex.Pattern;

public class ForgotPassword_Fragment extends Fragment implements
        View.OnClickListener {
    private static View view;
    private static EditText inputEmail;
    private static TextView submit, back;

    public ForgotPassword_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(com.appnucleus.loginandregisteruser.R.layout.forgotpassword_layout, container,
                false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    private void initViews() {
        inputEmail = (EditText) view.findViewById(com.appnucleus.loginandregisteruser.R.id.registered_emailid);
        submit = (TextView) view.findViewById(com.appnucleus.loginandregisteruser.R.id.forgot_button);
        back = (TextView) view.findViewById(com.appnucleus.loginandregisteruser.R.id.backToLoginBtn);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(com.appnucleus.loginandregisteruser.R.xml.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            back.setTextColor(csl);
            submit.setTextColor(csl);

        } catch (Exception e) {
        }

    }

    public class Utils {

        //Email Validation pattern
        public static final String regEx = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,4}";

        //Fragments Tags
        public static final String Activity_Login = "Activity_Login";
        public static final String Activity_Register = "Activity_Register";
        public static final String ForgotPassword_Fragment = "ForgotPassword_Fragment";
    }


    // Set Listeners over buttons
    private void setListeners() {
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
         /*   case R.id.backToLoginBtn:

                // Replace Login Fragment on Back Presses
                new Activity_Main().replaceLoginFragment();
                break;
*/
            case com.appnucleus.loginandregisteruser.R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

        }

    }

    private void submitButtonTask() {
        String getEmailId = inputEmail.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher((CharSequence) inputEmail);

        // First check if email id is not null else show error toast
        if (inputEmail.equals("") || inputEmail.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch passwod or do your stuff
        else
            Toast.makeText(getActivity(), "Get Forgot Password.",
                    Toast.LENGTH_SHORT).show();
    }
}
