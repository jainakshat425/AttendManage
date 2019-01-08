package com.example.android.attendmanage.utilities;

import com.example.android.attendmanage.pojos.Branch;
import com.example.android.attendmanage.pojos.Class;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Akshat Jain on 29-Dec-18.
 */
public class GsonUtils {

    public static ArrayList<Branch> extractBranchesFromJson(JSONObject jObj) {

        try {
            String recordsArray = jObj.getString("branches");

            Gson gson = new Gson();
            Branch[] targetArray = gson.fromJson(recordsArray, Branch[].class);

            return new ArrayList<>(Arrays.asList(targetArray));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Class> extractClassesFromJson(JSONObject jObj) {

        try {
            String recordsArray = jObj.getString("classes");

            Gson gson = new Gson();
            Class[] targetArray = gson.fromJson(recordsArray, Class[].class);

            return new ArrayList<>(Arrays.asList(targetArray));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
