package com.atomicity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
    @Bind(R.id.link_forgot) TextView link_forgot;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = LoginActivity.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
        link_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotPasswordIntent = new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(forgotPasswordIntent);
                LoginActivity.this.finish();
            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        authenticateUser(email,password);

                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish();
//            }
//        }
//    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
//        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void authenticateUser(final String email, final String password) {
        String url = Config.login_url;
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
                                JSONObject userDetails = jsonObject.getJSONObject("userdetails");
                                editor = sharedPreferences.edit();
                                editor.putString("id",userDetails.getString("id"));
                                editor.putString("first_name",userDetails.getString("first_name"));
                                editor.putString("last_name",userDetails.getString("last_name"));
                                editor.putString("email_id",userDetails.getString("email_id"));
                                editor.putString("address",userDetails.getString("address"));
                                editor.putString("description",userDetails.getString("description"));
                                editor.putString("hash_key",userDetails.getString("hash_key"));
//                                editor.putString("bloodGroup",jsonObject.getString("bg"));
//                                editor.putString("ailment",jsonObject.getString("ailment"));
                                editor.apply();
                                Intent mainActivityIntent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(mainActivityIntent);
                                LoginActivity.this.finish();
                            }
                            else {
                                new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("" +jsonObject.getString("reason"))
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
                        new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE)
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
                params.put("emailid", email);
                params.put("password", password);
                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(postRequest);
    }
}
