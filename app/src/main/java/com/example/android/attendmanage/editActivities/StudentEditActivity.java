package com.example.android.attendmanage.editActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.android.attendmanage.ClassSelectActivity;
import com.example.android.attendmanage.R;
import com.example.android.attendmanage.SharedPrefManager;
import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.pojos.Student;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;
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

public class StudentEditActivity extends AppCompatActivity {

    private int collId;
    private int stdId = -1;

    @BindView(R.id.std_edit_layout)
    LinearLayout layout;

    /**
     * semester
     */
    @BindView(R.id.std_edit_semester_spin)
    Spinner semesterSpinner;
    private String semester = "";

    /**
     * branch
     */
    @BindView(R.id.std_edit_branch_spin)
    Spinner branchSpinner;
    private String branch = "";
    private SpinnerArrayAdapter branchAdapter;

    /**
     * section
     */
    @BindView(R.id.std_edit_section_spin)
    Spinner sectionSpinner;
    private String section = "";
    private SpinnerArrayAdapter sectionAdapter;


    @BindView(R.id.std_roll_no_input)
    TextInputLayout rollNoIn;
    private String rollNo = "";

    @BindView(R.id.std_name_input)
    TextInputLayout nameIn;
    private String name = "";

    @OnClick(R.id.std_edit_done_fab)
    void saveStudent() {

        rollNo = Objects.requireNonNull(rollNoIn.getEditText()).getText().toString().trim();
        name = Objects.requireNonNull(nameIn.getEditText()).getText().toString().trim();

        if (validateInputs()) {
            Gson gson = new Gson();
            String stdJson = gson.toJson(new Student(stdId, rollNo, name, Integer.parseInt(semester), branch,
                    section));

            VolleyTask.saveStudent(this, collId, stdJson);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_edit);
        ButterKnife.bind(this);

        collId = SharedPrefManager.getInstance(this).getCollId();

        int mode = getIntent().getIntExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);

        if (mode == ExtraUtils.MODE_UPDATE) {

            Student student = getIntent().getParcelableExtra(ExtraUtils.EXTRA_STUDENT_OBJ);
            stdId = student.getStudentId();

            Objects.requireNonNull(rollNoIn.getEditText()).setText(student.getStdRollNo());
            Objects.requireNonNull(nameIn.getEditText()).setText(student.getStdName());

            semester = String.valueOf(student.getSemester());
            branch = student.getBName();
            section = student.getSection();
        }

        setupBranchSpinner();
        setupSemesterSpinner();
        setupSectionSpinner();

        refreshBranchSpinner();
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
                branchAdapter = new SpinnerArrayAdapter(StudentEditActivity.this,
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

    private void setupSemesterSpinner() {
        String[] semArr = getResources().getStringArray(R.array.semester_array);
        SpinnerArrayAdapter semesterAdapter = new SpinnerArrayAdapter(StudentEditActivity.this,
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
                    refreshSectionsSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupBranchSpinner() {
        List<String> brList = new ArrayList<>();
        brList.add("Branch");
        String[] brArr = brList.toArray(new String[0]);
        branchAdapter = new SpinnerArrayAdapter(StudentEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                brArr);
        branchSpinner.setAdapter(branchAdapter);

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0) {
                    branch = (String) parent.getItemAtPosition(pos);
                    refreshSectionsSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSectionSpinner() {
        String[] secArr = {"Section"};
        sectionAdapter = new SpinnerArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, secArr);
        sectionSpinner.setAdapter(sectionAdapter);

        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0)
                    section = (String) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void refreshSectionsSpinner() {
        if (!TextUtils.isEmpty(semester) && !TextUtils.isEmpty(branch)) {
            final List<String> secList = new ArrayList<>();
            secList.add("Section");
            VolleyTask.getSections(StudentEditActivity.this, branch,
                    semester, collId, jObj -> {

                        try {
                            JSONArray jsonArray = jObj.getJSONArray("sections");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                secList.add(jsonArray.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String[] secArr = secList.toArray(new String[0]);
                        sectionAdapter = new SpinnerArrayAdapter(StudentEditActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                secArr);
                        sectionSpinner.setAdapter(sectionAdapter);

                        if (section != null) {
                            for (int i = 0; i < secArr.length; i++) {
                                if (secArr[i].equals(section)) {
                                    sectionSpinner.setSelection(i);
                                    break;
                                }
                            }
                        }

                    });
        } else
            setupSectionSpinner();
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(name) || name.length() < 3) {
            nameIn.setError("Enter valid name.");
            return false;
        } else if (TextUtils.isEmpty(rollNo)) {
            nameIn.setError(null);
            rollNoIn.setError("Enter valid roll number.");
            return false;
        } else {
            nameIn.setError(null);
            rollNoIn.setError(null);
        }

        if (TextUtils.isEmpty(semester)) {
            Snackbar.make(layout, "Semester not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(branch)) {
            Snackbar.make(layout, "Branch not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(section)) {
            Snackbar.make(layout, "Section not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


}
