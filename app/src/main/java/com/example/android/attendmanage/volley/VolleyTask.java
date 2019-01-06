package com.example.android.attendmanage.volley;


import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.attendmanage.SharedPrefManager;
import com.example.android.attendmanage.utilities.ExtraUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Akshat Jain on 29-Dec-18.
 */
public class VolleyTask {

    public static void login(final Context context, final String username, final String password,
                                   final VolleyCallback volleyCallback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.ADMIN_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);

                            if (!jObj.getBoolean("error")) {
                                volleyCallback.onSuccessResponse(jObj);
                            } else {
                                Toast.makeText(context, jObj.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(SharedPrefManager.ADMIN_ID, username);
                params.put(SharedPrefManager.ADMIN_PASS, password);

                return params;
            }
        };

        RequestHandler.getInstance(context).addToRequestQueue(request);
    }
}
