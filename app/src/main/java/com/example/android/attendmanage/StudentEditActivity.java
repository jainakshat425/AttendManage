package com.example.android.attendmanage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.pojos.Student;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;
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

    /**
     * semester
     */
    @BindView(R.id.std_edit_semester_spin)
    Spinner semesterSpinner;
    private String semester = null;

    /**
     * branch
     */
    @BindView(R.id.std_edit_branch_spin)
    Spinner branchSpinner;
    private String branch = null;
    private SpinnerArrayAdapter branchAdapter;

    /**
     * section
     */
    @BindView(R.id.std_edit_section_spin)
    Spinner sectionSpinner;
    private String section = null;


    @BindView(R.id.std_roll_no_input)
    TextInputLayout rollNoIn;

    @BindView(R.id.std_name_input)
    TextInputLayout nameIn;


    @OnClick(R.id.std_edit_done_fab)
    void saveStudent() {

        String rollNo = Objects.requireNonNull(rollNoIn.getEditText()).getText().toString().trim();
        String name = Objects.requireNonNull(nameIn.getEditText()).getText().toString().trim();

        Gson gson = new Gson();
        String stdJson = gson.toJson(new Student(rollNo, name, Integer.parseInt(semester), branch,
                section));

        VolleyTask.saveStudent(this, collId, stdId, stdJson);
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

        VolleyTask.setupBranchSpinner(this, collId, jObj -> {
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

        setupSemesterSpinner();
        ExtraUtils.emptySectionSpinner(this, sectionSpinner);

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

    private void refreshSectionsSpinner() {
        if (semester != null && branch != null) {
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
                        SpinnerArrayAdapter sectionAdapter =
                                new SpinnerArrayAdapter(StudentEditActivity.this,
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
            ExtraUtils.emptySectionSpinner(this, sectionSpinner);
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
    }
}
