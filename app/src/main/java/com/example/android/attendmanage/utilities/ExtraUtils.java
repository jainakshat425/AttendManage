package com.example.android.attendmanage.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExtraUtils {

    public static final SimpleDateFormat dayFormat =
            new SimpleDateFormat("EEEE", Locale.US);

    public static final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm:ss", Locale.US);

    public static final SimpleDateFormat timeDisplayFormat =
            new SimpleDateFormat("hh:mm a", Locale.US);

    public static final String EXTRA_SEMESTER = "extra_semester";
    public static final String EXTRA_BRANCH = "extra_branch";
    public static final String EXTRA_SECTION = "extra_section";
    public static final String EXTRA_CLASS_DETAILS = "extra_class_details";


    public static final String EXTRA_FAC_EMAIL = "extra_fac_email";

    public static final String EXTRA_BRANCH_OBJ = "extra_branch_obj";
    public static final String EXTRA_CLASS_OBJ = "extra_class_obj";
    public static final String EXTRA_SUBJECT_OBJ = "extra_subject_obj";
    public static final String EXTRA_STUDENT_OBJ = "extra_student_obj";
    public static final String EXTRA_FAC_SCH_OBJ = "extra_fac_sch_obj";
    public static final String EXTRA_FACULTY_OBJ = "extra_faculty_obj";


    public static final String EXTRA_EDIT_MODE = "extra_edit_mode";
    public static final int MODE_UPDATE = 1;
    public static final int MODE_NEW = 2;


    private static final String DB_URL = "http://192.168.42.112/AttendManagePHP/CollegeAppPHP/";

    public static final String COLLEGE_LOGIN_URL = DB_URL + "collegeLogin.php";
    public static final String REGISTER_URL = DB_URL + "registerCollege.php";
    public static final String CHANGE_COLLEGE_PASS_URL = DB_URL + "changeCollegePassword.php";
    public static final String UPDATE_COLLEGE_DETAILS_URL = DB_URL + "updateCollegeDetails.php";

    public static final String GET_BRANCH_NAMES_URL = DB_URL + "getBranchNames.php";
    public static final String GET_SECS_URL = DB_URL + "getSections.php";
    public static final String GET_FAC_EMAILS = DB_URL + "getFacEmails.php";
    public static final String GET_SUBJECT_NAMES_URL = DB_URL + "getSubjectNames.php";

    public static final String DELETE_BRANCHES_URL = DB_URL + "deleteBranches.php";
    public static final String GET_BRANCHES_URL = DB_URL + "getBranches.php";
    public static final String SAVE_BRANCH_URL = DB_URL + "saveBranch.php";

    public static final String DELETE_CLASSES_URL = DB_URL + "deleteClasses.php";
    public static final String GET_CLASSES_URL = DB_URL + "getClasses.php";
    public static final String SAVE_CLASS_URL = DB_URL + "saveClass.php";

    public static final String GET_SUBJECTS_URL = DB_URL + "getSubjects.php";
    public static final String DELETE_SUBJECTS_URL = DB_URL + "deleteSubjects.php";
    public static final String SAVE_SUBJECT_URL = DB_URL + "saveSubject.php";

    public static final String GET_STUDENTS_URL = DB_URL + "getStudents.php";
    public static final String DELETE_STUDENTS_URL = DB_URL + "deleteStudents.php";
    public static final String SAVE_STUDENT_URL = DB_URL + "saveStudent.php";

    public static final String GET_FAC_SCH_URL = DB_URL + "getFacSchedules.php";
    public static final String DELETE_FAC_SCHS_URL = DB_URL + "deleteFacSchs.php";
    public static final String SAVE_FAC_SCH_URL = DB_URL + "saveFacSch.php";

    public static final String GET_FACULTIES_URL = DB_URL + "getFaculties.php";
    public static final String DELETE_FACULTY_URL = DB_URL + "deleteFaculties.php";
    public static final String SAVE_FACULTY_URL = DB_URL + "saveFaculty.php";

    public static String getSemester(String semester) {
        if (Integer.parseInt(semester) == 1) {
            semester = semester + "st Sem";
        } else if (Integer.parseInt(semester) == 2) {
            semester = semester + "nd Sem";
        } else if (Integer.parseInt(semester) == 3) {
            semester = semester + "rd Sem";
        } else {
            semester = semester + "th Sem";
        }
        return semester;
    }

    public static String getCurrentDay() {
        String day = dayFormat.format(Calendar.getInstance().getTime());
        return day.toUpperCase();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

