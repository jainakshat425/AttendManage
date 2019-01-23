package com.example.android.attendmanage.volley;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.android.attendmanage.R;
import com.example.android.attendmanage.utilities.ExtraUtils;

public class RequestHandler {
    private static RequestHandler mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private RequestHandler(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestHandler(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (ExtraUtils.isNetworkAvailable(mCtx))
            getRequestQueue().add(req);
        else
            Toast.makeText(mCtx, R.string.network_not_available, Toast.LENGTH_SHORT).show();
    }

}
