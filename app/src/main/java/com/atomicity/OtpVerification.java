package com.atomicity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class OtpVerification extends AppCompatActivity {
    @Bind(R.id.inputOtp) EditText otpEt;
    @Bind(R.id.btn_verify_otp) Button confirm;
//    SharedPreferences preferences;
    String code,email,codeToSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        ButterKnife.bind(this);
//        preferences = OtpVerification.this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        Intent intent = getIntent();
        code = intent.getStringExtra("code");
        email = intent.getStringExtra("emailId");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyUser();
            }
        });
    }
    private void verifyUser()
    {
        String url = Config.otpVerifyUrl;
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
                                new SweetAlertDialog(OtpVerification.this,SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Welcome !!")
                                        .setContentText("Account Verified Sucessfully")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                Intent mainActivityIntent = new Intent(OtpVerification.this,LoginActivity.class);
                                                startActivity(mainActivityIntent);
                                                sDialog.dismissWithAnimation();
                                                OtpVerification.this.finish();
                                            }
                                        })
                                        .show();

                            }
                            else {
                                new SweetAlertDialog(OtpVerification.this,SweetAlertDialog.ERROR_TYPE)
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
                        new SweetAlertDialog(OtpVerification.this,SweetAlertDialog.ERROR_TYPE)
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
                codeToSend = otpEt.getText().toString();
                params.put("code",codeToSend);
                params.put("emailid",email);
                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.getCache().clear();
        queue.add(postRequest);
    }
}
