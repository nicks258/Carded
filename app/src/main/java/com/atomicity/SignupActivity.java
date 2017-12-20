package com.atomicity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private NotificationCompat.Builder notificationBuilder;
    private Bitmap icon;
    int currentNotificationID = 0;
    private NotificationManager notificationManager;
    @Bind(R.id.input_fname) EditText _fnameText;
    @Bind(R.id.input_lname) EditText _lnameText;
    @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup) Button _signupButton;
    @Bind(R.id.link_login) TextView _loginLink;
    @Bind(R.id.description) EditText _description;
//    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
    String fname,lname,address,email, mobile,password ,reEnterPassword ,description;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
//        preferences = SignupActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        icon = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher);
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        registerUser();

//        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        fname = _fnameText.getText().toString();
        lname = _lnameText.getText().toString();
        address = _addressText.getText().toString();
        email = _emailText.getText().toString();
        Logger.i("Validate EmailVa->" + email);
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        reEnterPassword = _reEnterPasswordText.getText().toString();


        if (fname.isEmpty() || fname.length() < 3) {
            _fnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _fnameText.setError(null);
        }

        if (lname.isEmpty() || lname.length() < 3) {
            _lnameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lnameText.setError(null);
        }

        if (address.isEmpty()) {
            _addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _addressText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length()!=10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }

    private void registerUser()
    {
                fname = _fnameText.getText().toString();
                lname = _lnameText.getText().toString();
                address = _addressText.getText().toString();
                email = _emailText.getText().toString();
                Logger.i("Email->>>" + email);
                mobile = _mobileText.getText().toString();
                Logger.i("Mobile->>>" + mobile);
                password = _passwordText.getText().toString();
                reEnterPassword = _reEnterPasswordText.getText().toString();
                description = _description.getText().toString();

        String url = Config.registerUrl;
        Log.i("URL->",url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Log.d("Response-> ", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Logger.json(jsonObject.toString());
                            if (java.util.Objects.equals(jsonObject.getString("status"), "true"))
                            {
//                                preferences.edit().remove("user_email").apply();
                                final String emailId = jsonObject.getString("email");
                                final String code    = jsonObject.getString("code");
                                notificationBuilder = new NotificationCompat.Builder(SignupActivity.this)
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setLargeIcon(icon)
                                        .setContentTitle("Verify its you")
                                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Voila"))
                                        .setContentText("Your OTP is " + code);
                                Intent notificationIntent = new Intent(SignupActivity.this, OtpVerification.class);
                                notificationIntent.putExtra("emailId",emailId);
                                PendingIntent contentIntent = PendingIntent.getActivity(SignupActivity.this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                notificationBuilder.setContentIntent(contentIntent);
                                Notification notification = notificationBuilder.build();
                                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                                notification.defaults |= Notification.DEFAULT_SOUND;

                                currentNotificationID++;
                                int notificationId = currentNotificationID;
                                if (notificationId == Integer.MAX_VALUE - 1)
                                    notificationId = 0;
                                notificationManager.notify(notificationId, notification);

//                                editor         = preferences.edit();
//                                editor.putString("user_email",emailId);
//                                editor.putString("code",code);
//                                editor.apply();
                                new SweetAlertDialog(SignupActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Account Created")
                                        .setContentText("Check Email for your Verification Code")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                Intent mainActivityIntent = new Intent(SignupActivity.this,OtpVerification.class);
                                                mainActivityIntent.putExtra("emailId",emailId);
                                                mainActivityIntent.putExtra("code",code);
                                                startActivity(mainActivityIntent);
                                                sDialog.dismissWithAnimation();
                                                SignupActivity.this.finish();
                                            }
                                        })
                                        .show();

                            }
                            else {
                                new SweetAlertDialog(SignupActivity.this,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText(""+jsonObject.getString("reason") )
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        new SweetAlertDialog(SignupActivity.this,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Something went wrong!")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<String, String>();
                params.put("firstname",_fnameText.getText().toString());
                params.put("lastname", _lnameText.getText().toString());
                params.put("emailid", _emailText.getText().toString());
                params.put("address",_addressText.getText().toString());
                params.put("mobile",_mobileText.getText().toString());
                params.put("description", _description.getText().toString());
                params.put("password", _passwordText.getText().toString());
                Logger.i("->"+params.toString());
                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(postRequest);
    }
}