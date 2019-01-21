package com.example.android.attendmanage.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.android.attendmanage.editActivities.BranchEditActivity;
import com.example.android.attendmanage.editActivities.ClassEditActivity;
import com.example.android.attendmanage.editActivities.FacSchEditActivity;
import com.example.android.attendmanage.editActivities.FacultyEditActivity;
import com.example.android.attendmanage.SharedPrefManager;
import com.example.android.attendmanage.editActivities.StudentEditActivity;
import com.example.android.attendmanage.editActivities.SubjectEditActivity;
import com.example.android.attendmanage.utilities.ExtraUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Akshat Jain on 06-Jan-19.
 */
public class VolleyTask {

    public static void login(final Context context, final String username, final String password,
                             final VolleyCallback volleyCallback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Logging in...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.COLLEGE_LOGIN_URL,
                response -> {
                    pDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error")) {
                            volleyCallback.onSuccessResponse(jObj);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(SharedPrefManager.COLL_EMAIL, username);
                params.put(SharedPrefManager.COLL_PASS, password);

                return params;
            }
        };

        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void deleteBranches(Context context, List<Integer> branchIdList,
                                      VolleyCallback volleyCallback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Deleting...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_BRANCHES_URL,
                response -> {
            pDialog.dismiss();
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
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray jArr = new JSONArray(branchIdList);
                params.put("branch_id_list", jArr.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getBranches(final Context mContext, final int collId,
                                   VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_BRANCHES_URL,
                response -> {
            pDialog.dismiss();
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
            pDialog.dismiss();
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

    public static void deleteClasses(Context context, List<Integer> classIdList,
                                     VolleyCallback volleyCallback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Deleting...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_CLASSES_URL,
                response -> {
            pDialog.dismiss();
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
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray jArr = new JSONArray(classIdList);
                params.put("class_id_list", jArr.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getClasses(final Context mContext, final int collId,
                                  VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_CLASSES_URL,
                response -> {
            pDialog.dismiss();
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
            pDialog.dismiss();
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

    public static void getBranchNames(Context context, int collegeId,
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
                }, error ->
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put(SharedPrefManager.COLL_ID, String.valueOf(collegeId));

                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getSubjects(final Context mContext, final int collId,
                                   VolleyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_SUBJECTS_URL,
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

    public static void deleteSubjects(Context context, List<Integer> subIdList,
                                      VolleyCallback volleyCallback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Deleting...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_SUBJECTS_URL,
                response -> {
            pDialog.dismiss();
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
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray jArr = new JSONArray(subIdList);
                params.put("sub_id_list", jArr.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void saveSubject(Context context, int collId, int subId, String subJson) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.SAVE_SUBJECT_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context,
                                jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error"))
                            ((SubjectEditActivity) context).finish();

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
                if (subId != -1) {
                    params.put("sub_id", String.valueOf(subId));
                }
                params.put("college_id", String.valueOf(collId));
                params.put("subject_obj", subJson);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getSections(final Context mContext, final String branch,
                                   final String semester, final int collId,
                                   VolleyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_SECS_URL, response -> {
            try {
                JSONObject jObj = new JSONObject(response);

                if (!jObj.getBoolean("error")) {

                    callback.onSuccessResponse(jObj);
                } else {
                    Toast.makeText(mContext, jObj.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(mContext, error.getMessage(),
                Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("branch_name", branch);
                params.put("semester", semester);
                params.put("college_id", String.valueOf(collId));
                return params;
            }
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(request);

    }

    public static void getStudents(final Context mContext, int collId, String semester, String branch,
                                   String section, VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_STUDENTS_URL, response -> {
            pDialog.dismiss();
            try {
                JSONObject jObj = new JSONObject(response);

                if (!jObj.getBoolean("error")) {

                    callback.onSuccessResponse(jObj);
                } else {
                    Toast.makeText(mContext, jObj.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            pDialog.dismiss();
            Toast.makeText(mContext, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("branch_name", branch);
                params.put("semester", semester);
                params.put("section", section);
                params.put("college_id", String.valueOf(collId));
                return params;
            }
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(request);
    }

    public static void deleteStudents(Context context, List<Integer> stdIdList, VolleyCallback volleyCallback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Deleting...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_STUDENTS_URL,
                response -> {
            pDialog.dismiss();
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
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray jArr = new JSONArray(stdIdList);
                params.put("std_id_list", jArr.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void saveStudent(Context context, int collId, int stdId, String stdJson) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.SAVE_STUDENT_URL,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context,
                                jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error"))
                            ((StudentEditActivity) context).finish();

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
                if (stdId != -1) {
                    params.put("student_id", String.valueOf(stdId));
                }
                params.put("college_id", String.valueOf(collId));
                params.put("student_obj", stdJson);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getFacUserIds(final Context mContext, final int collId,
                                     VolleyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_FAC_USER_IDS,
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

    public static void getFacSchedule(final Context mContext, final String facUserId, final String day,
                                      VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_FAC_SCH_URL,
                response -> {
            pDialog.dismiss();
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
            pDialog.dismiss();
            Toast.makeText(mContext, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fac_user_id", facUserId);
                params.put("day", day);
                return params;
            }
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(request);
    }

    public static void deleteFacSchs(Context context, List<Integer> lectIdList,
                                     VolleyCallback volleyCallback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Deleting...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_FAC_SCHS_URL,
                response -> {
            pDialog.dismiss();
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
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray jArr = new JSONArray(lectIdList);
                params.put("lect_id_list", jArr.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void saveFacSch(Context context, int collId, int lectId, String facSchJson) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.SAVE_FAC_SCH_URL,
                response -> {
                    pDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context,
                                jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error"))
                            ((FacSchEditActivity) context).finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (lectId != -1) {
                    params.put("lect_id", String.valueOf(lectId));
                }
                params.put("college_id", String.valueOf(collId));
                params.put("fac_sch_obj", facSchJson);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void getSubjectNames(final Context mContext, final String branch,
                                       final String semester, final int collId,
                                       VolleyCallback callback) {
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_SUBJECT_NAMES_URL, response -> {
            try {
                JSONObject jObj = new JSONObject(response);

                if (!jObj.getBoolean("error")) {

                    callback.onSuccessResponse(jObj);
                } else {
                    Toast.makeText(mContext, jObj.getString("message"),
                            Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(mContext, error.getMessage(),
                Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("branch_name", branch);
                params.put("semester", semester);
                params.put("college_id", String.valueOf(collId));
                return params;
            }
        };
        RequestHandler.getInstance(mContext).addToRequestQueue(request);

    }

    public static void getFaculties(final Context mContext, final int collId,
                                    VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.GET_FACULTIES_URL,
                response -> {
            pDialog.dismiss();
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
            pDialog.dismiss();
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

    public static void deleteFaculties(Context context, List<Integer> facIdList,
                                       VolleyCallback volleyCallback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Deleting...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.DELETE_FACULTY_URL,
                response -> {
            pDialog.dismiss();
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
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                JSONArray jArr = new JSONArray(facIdList);
                params.put("fac_id_list", jArr.toString());
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void saveFaculty(Context context, int collId, String facJson) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.SAVE_FACULTY_URL,
                response -> {
                    pDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context,
                                jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error"))
                            ((FacultyEditActivity) context).finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("college_id", String.valueOf(collId));
                params.put("faculty_obj", facJson);
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                    0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void registerCollege(Context context, String email,
                                       String pass, String collName,
                                       String collFullName, VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Registering...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.REGISTER_URL,
                response -> {
                    pDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error")) {
                            callback.onSuccessResponse(jObj);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", pass);
                params.put("coll_name", collName);
                params.put("coll_full_name", collFullName);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,
                0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void changeAdminPassword(Context context, int collId, String currentPass,
                                           String newPass, VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Verifying & Updating...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.CHANGE_COLLEGE_PASS_URL,
                response -> {
                    pDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error")) {
                            callback.onSuccessResponse(jObj);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("coll_id", String.valueOf(collId));
                params.put("current_password", currentPass);
                params.put("new_password", newPass);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }

    public static void updateCollegeDetails(Context context, int collId, String password,
                                            String collName, String collFullName,
                                            String email, VolleyCallback callback) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Updating...");
        pDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                ExtraUtils.UPDATE_COLLEGE_DETAILS_URL,
                response -> {
                    pDialog.dismiss();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        Toast.makeText(context, jObj.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (!jObj.getBoolean("error")) {
                            callback.onSuccessResponse(jObj);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            pDialog.dismiss();
            Toast.makeText(context, error.getMessage(),
                    Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("coll_id", String.valueOf(collId));
                params.put("password", password);
                params.put("coll_name", collName);
                params.put("coll_full_name", collFullName);
                params.put("coll_email", email);
                return params;
            }
        };
        RequestHandler.getInstance(context).addToRequestQueue(request);
    }
}
