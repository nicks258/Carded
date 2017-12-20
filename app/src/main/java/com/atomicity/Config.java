package com.atomicity;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by sumitmehra on 24-11-2017.
 */

public class Config {
//    All Variables
    private static String parentUrl="http://suvojitkar365.esy.es/v1/user/";
    static String login_url=parentUrl +"login/";
    public static String registerUrl=parentUrl + "register/";
    public static String otpVerifyUrl=parentUrl + "verification/registration/";
    public static String initiliseForgotPassword=parentUrl + "forgotpassword/";
    public static String updateForgotPassword=parentUrl + "updatepassword/";
    public static String verifyUpdatePassword=parentUrl + "verification/password/";
    public static String homeUrl=parentUrl + "";
    public static JSONObject jsonObject;
    private static String lat       = "19.1973,";
    private static String longitude = "72.9567";
    public static String URL_FEED = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
            "location=";






//    Public and Common Functions
IResult mResultCallback = null;
    Context mContext;

    public Config(IResult resultCallback, Context context){
        mResultCallback = resultCallback;
        mContext = context;
    }


//    public void postDataVolley(final String requestType, String url,JSONObject sendObj){
//        try {
//            RequestQueue queue = Volley.newRequestQueue(mContext);
//
//            JsonObjectRequest jsonObj = new JsonObjectRequest(url,sendObj, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    if(mResultCallback != null)
//                        mResultCallback.notifySuccess(requestType,response);
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    if(mResultCallback != null)
//                        mResultCallback.notifyError(requestType,error);
//                }
//            })
//            {
//                @Override
//                protected Map<String, String> getParams()
//                {
//                    Map<String, String>  params = new HashMap<String, String>();
//                    params.put("name", "Alif");
//                    params.put("domain", "http://itsalif.info");
//                    return params;
//                }
//            };
//
//            queue.add(jsonObj);
//
//        }catch(Exception e){
//
//        }
//    }

    public void getDataVolley(final String requestType, String url){
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if(mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            });

            queue.add(jsonObj);

        }catch(Exception e){

        }
    }
}
