package com.atomicity;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by sumitmehra on 27-11-2017.
 */

public interface IResult {
    public void notifySuccess(String requestType, JSONObject response);
    public void notifyError(String requestType, VolleyError error);
}
