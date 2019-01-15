package com.example.android.attendmanage.utilities;

import android.content.Context;
import android.widget.Spinner;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExtraUtils {

    public static final  SimpleDateFormat dateDisplayFormat =
            new SimpleDateFormat("MMMM dd, yyyy", Locale.US);

    public static final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static final SimpleDateFormat dayFormat =
            new SimpleDateFormat("EEEE", Locale.US);

    public static final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm:ss", Locale.US);

    public static final SimpleDateFormat timeDisplayFormat =
            new SimpleDateFormat("hh:mm a", Locale.US);

    public static final String EXTRA_COLLEGE_ID = "extra_college_id";
    public static final String EXTRA_CLASS_ID = "extra_class_id";
    public static final String EXTRA_BRANCH_ID = "extra_branch_id";
    public static final String EXTRA_SUBJECT_ID = "extra_subject_id";
    public static final String EXTRA_ATTEND_REC_ID = "extra_attend_rec_id";

    public static final String EXTRA_COLLEGE = "extra_college";
    public static final String EXTRA_SEMESTER = "extra_semester";
    public static final String EXTRA_BRANCH = "extra_branch";
    public static final String EXTRA_SECTION = "extra_section";

    public static final String EXTRA_DATE = "extra_date";
    public static final String EXTRA_DAY = "extra_day";
    public static final String EXTRA_DISPLAY_DATE = "extra_display_date" ;
    public static final String EXTRA_FROM_DATE = "extra_from_date" ;
    public static final String EXTRA_TO_DATE = "extra_to_date" ;
    public static final String EXTRA_IS_DATE_WISE = "extra_is_date_wise" ;

    public static final String EXTRA_SUBJECT = "extra_subject";
    public static final String EXTRA_LECTURE_NO = "extra_lecture";

    public static final String EXTRA_FAC_USER_ID = "extra_fac_user_id";
    public static final String EXTRA_FAC_NAME = "extra_fac_name";
    public static final String EXTRA_FAC_DEPT = "extra_fac_dept";


    public static final String EXTRA_BRANCH_OBJ = "extra_branch_obj";
    public static final String EXTRA_CLASS_OBJ = "extra_class_obj";
    public static final String EXTRA_SUBJECT_OBJ = "extra_subject_obj";
    public static final String EXTRA_STUDENT_OBJ = "extra_student_obj";
    public static final String EXTRA_FAC_SCH_OBJ = "extra_fac_sch_obj";
    public static final String EXTRA_FACULTY_OBJ = "extra_faculty_obj";


    public static final String EXTRA_EDIT_MODE = "extra_edit_mode";
    public static final int MODE_UPDATE = 1;
    public static final int MODE_NEW = 2;


    private static final String DB_URL = "http://192.168.43.156/attendmanagephp/v1/";

    public static final String ADMIN_LOGIN_URL = DB_URL + "adminLogin.php";
    public static final String GET_BRANCH_NAMES_URL = DB_URL + "getBranchNames.php";
    public static final String GET_SECS_URL = DB_URL + "getSections.php";
    public static final String GET_FAC_USER_IDS = DB_URL + "getFacUserIds.php";
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


    public static String getLecture(String lecture) {
        if (Integer.parseInt(lecture) == 1) {
            lecture = lecture + "st Lecture";
        } else if (Integer.parseInt(lecture) == 2) {
            lecture = lecture + "nd Lecture";
        } else if (Integer.parseInt(lecture) == 3) {
            lecture = lecture + "rd Lecture";
        } else {
            lecture = lecture + "th Lecture";
        }
        return lecture;
    }

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

    public static String getCurrentDate() {
        String date = dateFormat.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getCurrentDateDisplay() {
        String date = dateDisplayFormat.format(Calendar.getInstance().getTime());
        return date;
    }

    public static String getCurrentTime() {
        String time = timeFormat.format(Calendar.getInstance().getTime());
        return time;
    }

    public static String getCurrentTimeDisplay() {
        String time = timeFormat.format(Calendar.getInstance().getTime());
        return time;
    }

    /**
     * empties the subject spinner
     */
    public static void emptySubjectSpinner(Context context, Spinner subSpinner) {
        String[] subject = {"Subject"};
        SpinnerArrayAdapter subjectAdapter = new SpinnerArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, subject);
        subSpinner.setAdapter(subjectAdapter);
    }

    /**
     * empties the section spinner
     */
    public static void emptySectionSpinner(Context context, Spinner secSpinner) {
        String[] section = {"Section"};
        SpinnerArrayAdapter sectionAdapter = new SpinnerArrayAdapter(context,
                android.R.layout.simple_spinner_dropdown_item, section);
        secSpinner.setAdapter(sectionAdapter);
    }

}

