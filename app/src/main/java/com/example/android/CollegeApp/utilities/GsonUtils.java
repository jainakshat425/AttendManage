package com.example.android.CollegeApp.utilities;

import com.example.android.CollegeApp.pojos.Branch;
import com.example.android.CollegeApp.pojos.Class;
import com.example.android.CollegeApp.pojos.FacSchedule;
import com.example.android.CollegeApp.pojos.Faculty;
import com.example.android.CollegeApp.pojos.Student;
import com.example.android.CollegeApp.pojos.Subject;
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

            if (recordsArray != null && !recordsArray.equals("null")) {

                Gson gson = new Gson();
                Branch[] targetArray = gson.fromJson(recordsArray, Branch[].class);


                return new ArrayList<>(Arrays.asList(targetArray));
            } else return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Class> extractClassesFromJson(JSONObject jObj) {

        try {
            String recordsArray = jObj.getString("classes");

            if (recordsArray != null && !recordsArray.equals("null")) {

                Gson gson = new Gson();
                Class[] targetArray = gson.fromJson(recordsArray, Class[].class);

                return new ArrayList<>(Arrays.asList(targetArray));
            } else return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Subject> extractSubjectsFromJson(JSONObject jObj) {

        try {
            String recordsArray = jObj.getString("subjects");

            if (recordsArray != null && !recordsArray.equals("null")) {

                Gson gson = new Gson();
                Subject[] targetArray = gson.fromJson(recordsArray, Subject[].class);

                return new ArrayList<>(Arrays.asList(targetArray));
            } else return null;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Student> extractStudentsFromJson(JSONObject jObj) {

        try {
            String recordsArray = jObj.getString("students");

            if (recordsArray != null && !recordsArray.equals("null")) {

                Gson gson = new Gson();
                Student[] targetArray = gson.fromJson(recordsArray, Student[].class);
                return new ArrayList<>(Arrays.asList(targetArray));

            } else return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<FacSchedule> extractFacSchFromJson(JSONObject jObj) {

        try {
            String recordsArray = jObj.getString("schedule");

            if (recordsArray != null && !recordsArray.equals("null")) {

                Gson gson = new Gson();
                FacSchedule[] targetArray = gson.fromJson(recordsArray, FacSchedule[].class);
                return new ArrayList<>(Arrays.asList(targetArray));
            } else return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Faculty> extractFacultiesFromJson(JSONObject jObj) {

        try {
            String recordsArray = jObj.getString("faculties");

            if (recordsArray != null && !recordsArray.equals("null")) {

                Gson gson = new Gson();
                Faculty[] targetArray = gson.fromJson(recordsArray, Faculty[].class);
                return new ArrayList<>(Arrays.asList(targetArray));
            } else return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
