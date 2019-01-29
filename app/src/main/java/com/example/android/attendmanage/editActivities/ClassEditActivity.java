package com.example.android.attendmanage.editActivities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.android.attendmanage.R;
import com.example.android.attendmanage.SharedPrefManager;
import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.pojos.Class;
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

public class ClassEditActivity extends AppCompatActivity {

    private int collId;
    private int classId = -1;

    @BindView(R.id.class_edit_layout)
    LinearLayout layout;

    /**
     * semester
     */
    @BindView(R.id.class_sem_spin)
    Spinner semesterSpinner;
    private String semester = "";
    private SpinnerArrayAdapter semesterAdapter;


    /**
     * branch
     */
    @BindView(R.id.class_branch_spin)
    Spinner branchSpinner;
    private String branch = "";
    private SpinnerArrayAdapter branchAdapter;


    /**
     * section
     */
    @BindView(R.id.class_section_input)
    TextInputLayout sectionIn;
    String section = "";


    @OnClick(R.id.class_edit_done_fab)
    void saveClass() {

        section = Objects.requireNonNull(sectionIn.getEditText()).getText().toString().trim();

        if (validateInputs()) {
            Gson gson = new Gson();
            String classJson = gson.toJson(new Class(classId, Integer.parseInt(semester), section, branch));

            VolleyTask.saveClass(this, collId, classJson);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_edit);
        ButterKnife.bind(this);

        collId = SharedPrefManager.getInstance(this).getCollId();

        int mode = getIntent().getIntExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);

        if (mode == ExtraUtils.MODE_UPDATE) {

            Class mClass = getIntent().getParcelableExtra(ExtraUtils.EXTRA_CLASS_OBJ);
            classId = mClass.getClassId();

            Objects.requireNonNull(sectionIn.getEditText()).setText(mClass.getSection());
            semester = String.valueOf(mClass.getSemester());
            branch = mClass.getBranchName();
        }

        setupBranchSpinner();
        setupSemesterSpinner();

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
                branchAdapter = new SpinnerArrayAdapter(ClassEditActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        brArr);
                branchSpinner.setAdapter(branchAdapter);

                if (!TextUtils.isEmpty(branch)) {
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

    private void setupBranchSpinner() {
        List<String> brList = new ArrayList<>();
        brList.add("Branch");
        String[] brArr = brList.toArray(new String[0]);
        branchAdapter = new SpinnerArrayAdapter(ClassEditActivity.this,
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
        semesterAdapter = new SpinnerArrayAdapter(ClassEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                semArr);
        semesterSpinner.setAdapter(semesterAdapter);


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

        if (!TextUtils.isEmpty(semester)) {
            for (int i = 0; i < semArr.length; i++) {
                if (semArr[i].equals(semester)) {
                    semesterSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(semester)) {
            Snackbar.make(layout, "Semester not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(branch)) {
            Snackbar.make(layout, "Branch not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(section)) {
            sectionIn.setError("Enter valid section.");
            return false;
        } else {
            sectionIn.setError(null);
            return true;
        }
    }

}
