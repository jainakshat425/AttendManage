package com.example.android.CollegeApp.editActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.android.CollegeApp.R;
import com.example.android.CollegeApp.SharedPrefManager;
import com.example.android.CollegeApp.adapter.SpinnerArrayAdapter;
import com.example.android.CollegeApp.pojos.Subject;
import com.example.android.CollegeApp.utilities.ExtraUtils;
import com.example.android.CollegeApp.volley.VolleyTask;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectEditActivity extends AppCompatActivity {

    private int collId;
    private int subId = -1;

    @BindView(R.id.sub_edit_layout)
    LinearLayout layout;

    /**
     * semester
     */
    @BindView(R.id.sub_semester_spin)
    Spinner semesterSpinner;
    private String semester = "";


    /**
     * branch
     */
    @BindView(R.id.sub_branch_spin)
    Spinner branchSpinner;
    private String branch = "";
    private SpinnerArrayAdapter branchAdapter;


    @BindView(R.id.sub_name_input)
    TextInputLayout sNameIn;
    private String sName = "";

    @BindView(R.id.sub_full_name_input)
    TextInputLayout sFullNameIn;
    private String sFullName = "";

    @OnClick(R.id.sub_edit_done_fab)
    void saveSubject() {

        sName = Objects.requireNonNull(sNameIn.getEditText()).getText().toString().trim();
        sFullName = Objects.requireNonNull(sFullNameIn.getEditText()).getText().toString().trim();

        if (validateInputs()) {
            Gson gson = new Gson();
            String subJson = gson.toJson(new Subject(subId, semester, branch, sName, sFullName));

            VolleyTask.saveSubject(this, collId, subJson);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_edit);
        ButterKnife.bind(this);

        collId = SharedPrefManager.getInstance(this).getCollId();

        int mode = getIntent().getIntExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);

        if (mode == ExtraUtils.MODE_UPDATE) {

            Subject subject = getIntent().getParcelableExtra(ExtraUtils.EXTRA_SUBJECT_OBJ);
            subId = subject.getSubId();

            Objects.requireNonNull(sNameIn.getEditText()).setText(subject.getSubName());
            Objects.requireNonNull(sFullNameIn.getEditText()).setText(subject.getSubFullName());

            semester = String.valueOf(subject.getSubSemester());
            branch = subject.getbName();
        }

        setupBranchSpinner();

        setupSemesterSpinner();

        refreshBranchSpinner();
    }

    private void setupBranchSpinner() {
        List<String> brList = new ArrayList<>();
        brList.add("Branch");
        String[] brArr = brList.toArray(new String[0]);
        branchAdapter = new SpinnerArrayAdapter(SubjectEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                brArr);
        branchSpinner.setAdapter(branchAdapter);
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0) {
                    branch = (String) parent.getItemAtPosition(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSemesterSpinner() {
        String[] semArr = getResources().getStringArray(R.array.semester_array);
        SpinnerArrayAdapter semesterAdapter = new SpinnerArrayAdapter(SubjectEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                semArr);
        semesterSpinner.setAdapter(semesterAdapter);

        if (semester != null) {
            for (int i = 0; i < semArr.length; i++) {
                if (semArr[i].equals(semester)) {
                    semesterSpinner.setSelection(i);
                    break;
                }
            }
        }
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0) {
                    semester = (String) parent.getItemAtPosition(pos);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void refreshBranchSpinner() {
        VolleyTask.getBranchNames(this, collId, jObj -> {
            try {
                JSONArray brJsonArr = jObj.getJSONArray("branch_names");
                List<String> brList = new ArrayList<>();
                brList.add("Branch");
                for (int i = 0; i < brJsonArr.length(); i++) {
                    brList.add(brJsonArr.getString(i));
                }
                String[] brArr = brList.toArray(new String[0]);
                branchAdapter = new SpinnerArrayAdapter(SubjectEditActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        brArr);
                branchSpinner.setAdapter(branchAdapter);

                if (branch != null) {
                    for (int i = 0; i < brArr.length; i++) {
                        if (brArr[i].equals(branch)) {
                            branchSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(sName) || sName.length() < 2) {
            sNameIn.setError("Enter valid subject name.");
            return false;
        } else if (TextUtils.isEmpty(sFullName)) {
            sNameIn.setError(null);
            sFullNameIn.setError("Enter valid subject name.");
            return false;
        } else {
            sNameIn.setError(null);
            sFullNameIn.setError(null);
        }
        if (TextUtils.isEmpty(semester)) {
            Snackbar.make(layout, "Semester not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(branch)) {
            Snackbar.make(layout, "Branch not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
