package com.example.daddyz.turtleboys;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zachary.rodriguez on 6/22/2015.
 */
public class VolleyJSONObjectRequest extends JsonObjectRequest {

    public VolleyJSONObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        ParseUser currentUser = ParseUser.getCurrentUser();
        String apiKey = null;
        String sessionToken = null;

        try{
            apiKey = currentUser.getString("apiKey");
            sessionToken = currentUser.getSessionToken();
        } catch(NullPointerException err){
            throw new AuthFailureError();
        }

        Log.i("API key: ", apiKey);
        Log.i("Token: ", sessionToken);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Authorization", apiKey);
        headers.put("Token", sessionToken);
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }
}