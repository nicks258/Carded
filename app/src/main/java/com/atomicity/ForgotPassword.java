package com.atomicity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class ForgotPassword extends AppCompatActivity {
    @Bind(R.id.registered_emailid) EditText emailId;
    @Bind(R.id.forgot_button) TextView forgotButton;
    @Bind(R.id.backToLoginBtn) TextView backToLoginBtn;
    @Bind(R.id.code) EditText code;
    @Bind(R.id.updated_password) EditText updatedPassword;
    String email,id,codeText;
    int passwordUpdateSteps=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
//        getSupportActionBar().setBackgroundDrawable(
//                new ColorDrawable(Color.parseColor("#3b5bb3")));

//        ActionBar bar = getActionBar();
//        bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        email = emailId.getText().toString();
        code.setVisibility(View.GONE);
        updatedPassword.setVisibility(View.GONE);
        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordUpdateSteps==0)
                {
                    forgotPassword();
                }
                else if (passwordUpdateSteps==1)
                {
                    codeVerification();
                }
                else if (passwordUpdateSteps==2){
                    updatePassword();
                }
            }
        });
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(ForgotPassword.this,LoginActivity.class);
                startActivity(loginIntent);
                ForgotPassword.this.finish();
            }
        });
    }

    private void codeVerification(){
        String url = Config.verifyUpdatePassword;
        final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
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
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Logger.json(jsonObject.toString());
                            if (java.util.Objects.equals(jsonObject.getString("status"), "true"))
                            {

                                new SweetAlertDialog(ForgotPassword.this,SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Welcome !!")
                                        .setContentText("" + jsonObject.getString("reason"))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                updatedPassword.setVisibility(View.VISIBLE);
                                                passwordUpdateSteps= 2;
//                                                updatePassword();
                                            }
                                        })
                                        .show();

                            }
                            else {
                                new SweetAlertDialog(ForgotPassword.this,SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("" + jsonObject.getString("reason"))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                            }
                        } catch (Exception e) {
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
                        new SweetAlertDialog(ForgotPassword.this,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Network Error !!")
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
//                codeToSend = otpEt.getText().toString();
                params.put("code",code.getText().toString());
                params.put("id",id);
                Logger.i("->" + params.toString());
                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(postRequest);
    }

    private void updatePassword() {
//        String password = updatedPassword.getText().toString();
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            updatedPassword.setError("between 4 and 10 alphanumeric characters");
//        }
//        else
            {
            String url = Config.updateForgotPassword;
            final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();
            Log.i("URL->", url);
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Logger.addLogAdapter(new AndroidLogAdapter());
                            Log.d("Response-> ", response);
                            progressDialog.dismiss();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Logger.json(jsonObject.toString());
                                if (java.util.Objects.equals(jsonObject.getString("status"), "true")) {

                                    new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Password Updated !!")
                                            .setContentText("" + jsonObject.getString("reason"))
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                    Intent loginIntent = new Intent(ForgotPassword.this, LoginActivity.class);
                                                    startActivity(loginIntent);
                                                    ForgotPassword.this.finish();
                                                }
                                            })
                                            .show();

                                } else {
                                    new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("" + jsonObject.getString("reason"))
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismissWithAnimation();
                                                }
                                            })
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Log.d("Error.Response", error.toString());
                            new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Network Error !!")
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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
//                codeToSend = otpEt.getText().toString();
                    params.put("userid", id);
                    params.put("new_password", updatedPassword.getText().toString());
                    Logger.i("->" + params.toString());
                    return params;
                }
            };
            postRequest.setShouldCache(false);
            queue.getCache().clear();
            queue.add(postRequest);
        }
    }

    private void forgotPassword(){
        final ProgressDialog progressDialog = new ProgressDialog(ForgotPassword.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(ForgotPassword.this);
        final String url = Config.initiliseForgotPassword + emailId.getText().toString();
        Log.i("Url->" , url);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        progressDialog.dismiss();
                        Logger.addLogAdapter(new AndroidLogAdapter());
                        Logger.json(response.toString());
                        Log.d("Response", response.toString());

                        try {
                            if (java.util.Objects.equals(response.getString("status"), "true"))
                            {
                                passwordUpdateSteps=1;
                                id = response.getString("id");
                                codeText = response.getString("code");
                                new SweetAlertDialog(ForgotPassword.this,SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Forgot Password")
                                        .setContentText("" + response.getString("reason"))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                emailId.setEnabled(false);
                                                code.setVisibility(View.VISIBLE);
//                                                codeVerification();
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
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        // add it to the RequestQueue
        getRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(getRequest);
    }
}
