package com.example.android.attendmanage;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.pojos.Class;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;
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

    /**
     * semester
     */
    @BindView(R.id.class_sem_spin)
    Spinner semesterSpinner;
    private String semester = null;
    private SpinnerArrayAdapter semesterAdapter;


    /**
     * branch
     */
    @BindView(R.id.class_branch_spin)
    Spinner branchSpinner;
    private String branch = null;
    private SpinnerArrayAdapter branchAdapter;


    /**
     * section
     */
    @BindView(R.id.class_section_input)
    TextInputLayout sectionIn;


    @OnClick(R.id.class_edit_done_fab)
    void saveClass() {

        String section = Objects.requireNonNull(sectionIn.getEditText()).getText().toString().trim();

        Gson gson = new Gson();
        String classJson = gson.toJson(new Class(Integer.parseInt(semester), section, branch));

        VolleyTask.saveClass(this, collId, classId, classJson);
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

        VolleyTask.setupBranchSpinner(this, collId, jObj -> {
            try {
                JSONArray brJsonArr = jObj.getJSONArray("branch_names");
                List<String> brList = new ArrayList<>();
                brList.add("Branch");
                for(int i=0; i<brJsonArr.length(); i++) {
                    brList.add(brJsonArr.getString(i));
                }
                String[] brArr = brList.toArray(new String[0]);
                branchAdapter = new SpinnerArrayAdapter(ClassEditActivity.this,
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

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                branch = (String) parent.getItemAtPosition(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setupSemesterSpinner();
    }

    private void setupSemesterSpinner() {
        String[] semArr = getResources().getStringArray(R.array.semester_array);
        semesterAdapter = new SpinnerArrayAdapter(ClassEditActivity.this,
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
                semester = (String) parent.getItemAtPosition(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
