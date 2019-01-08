package com.example.android.attendmanage.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.android.attendmanage.BranchEditActivity;
import com.example.android.attendmanage.ClassEditActivity;
import com.example.android.attendmanage.SharedPrefManager;
import com.example.android.attendmanage.utilities.ExtraUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akshat Jain on 06-Jan-19.
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

    public static void setupHodSpinner(Context context, int collegeId,
                                       VolleyCallback volleyCallback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_FACS_URL,
                response -> {
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
                }, error -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(SharedPrefManager.COLL_ID, String.valueOf(collegeId));

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void deleteBranch(Context context, int branchId,
                                    VolleyCallback volleyCallback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_BRANCH_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        if (!jObj.getBoolean("error")) {
                            volleyCallback.onSuccessResponse(jObj);
                        }
                        Toast.makeText(context, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("branch_id", String.valueOf(branchId));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getBranches(final Context mContext, final int collId,
                                   VolleyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_BRANCHES_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);

                        if (!jObj.getBoolean("error")) {
                            callback.onSuccessResponse(jObj);
                        } else {
                            Toast.makeText(mContext,
                                    jObj.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(mContext, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("college_id", String.valueOf(collId));
                return params;
            }
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(request);
    }

    public static void saveBranch(Context context, int collId, int branchId, String branchJson) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.SAVE_BRANCH_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context,
                                jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error"))
                            ((BranchEditActivity) context).finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pDialog.dismiss();
                }, error -> {
            Toast.makeText(context, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (branchId != -1) {
                    params.put("branch_id", String.valueOf(branchId));
                } else {
                    params.put("college_id", String.valueOf(collId));
                }
                params.put("branch_obj", branchJson);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void deleteClass(Context context, int classId,
                                    VolleyCallback volleyCallback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_CLASS_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        if (!jObj.getBoolean("error")) {
                            volleyCallback.onSuccessResponse(jObj);
                        }
                        Toast.makeText(context, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_id", String.valueOf(classId));
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getClasses(final Context mContext, final int collId,
                                   VolleyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_CLASSES_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);

                        if (!jObj.getBoolean("error")) {
                            callback.onSuccessResponse(jObj);
                        } else {
                            Toast.makeText(mContext,
                                    jObj.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(mContext, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("college_id", String.valueOf(collId));
                return params;
            }
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(request);
    }

    public static void saveClass(Context context, int collId, int classId, String classJson) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.SAVE_CLASS_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context,
                                jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error"))
                            ((ClassEditActivity) context).finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    pDialog.dismiss();
                }, error -> {
            Toast.makeText(context, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (classId != -1) {
                    params.put("class_id", String.valueOf(classId));
                }
                params.put("college_id", String.valueOf(collId));
                params.put("class_obj", classJson);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void setupBranchSpinner(Context context, int collegeId,
                                       VolleyCallback volleyCallback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_BRANCH_NAMES_URL,
                response -> {
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
                }, error -> Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(SharedPrefManager.COLL_ID, String.valueOf(collegeId));

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }
}
