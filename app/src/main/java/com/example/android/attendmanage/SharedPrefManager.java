package com.example.android.attendmanage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String MY_SHARED_PREF = "credentialPref";
    public static final String COLL_ID = "_id";
    public static final String COLL_NAME = "coll_name";
    public static final String COLL_FULL_NAME = "coll_full_name";
    public static final String ADMIN_ID = "coll_admin_id";
    public static final String ADMIN_PASS = "coll_admin_pass";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean isLoggedIn() {

        SharedPreferences sharedPref = mCtx
                .getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);

        if (sharedPref.getString(ADMIN_ID, null) != null)
            return true;
        else
            return false;
    }

    public boolean saveAdminDetails(int collId, String collName, String collFullName,
                                        String adminId) {

        SharedPreferences.Editor sharedPref = mCtx.getSharedPreferences(MY_SHARED_PREF,
                Context.MODE_PRIVATE).edit();

        sharedPref.putInt(COLL_ID, collId);
        sharedPref.putString(COLL_NAME, collName);
        sharedPref.putString(COLL_FULL_NAME, collFullName);
        sharedPref.putString(ADMIN_ID, ADMIN_ID);

        sharedPref.apply();
        return true;
    }

    public int getCollId() {
        SharedPreferences sharedPref = mCtx
                .getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);

        return sharedPref.getInt(COLL_ID, -1);
    }

    public String getCollName() {
        SharedPreferences sharedPref = mCtx
                .getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);

        return sharedPref.getString(COLL_NAME, null);
    }

    public String getCollFullName() {
        SharedPreferences sharedPref = mCtx
                .getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);

        return sharedPref.getString(COLL_FULL_NAME, null);
    }

    public String getAdminId() {
        SharedPreferences sharedPref = mCtx
                .getSharedPreferences(MY_SHARED_PREF, Context.MODE_PRIVATE);

        return sharedPref.getString(ADMIN_ID, null);
    }

}
