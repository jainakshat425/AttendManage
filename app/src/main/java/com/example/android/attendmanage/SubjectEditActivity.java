package com.example.android.attendmanage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.android.attendmanage.pojos.Subject;
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

public class SubjectEditActivity extends AppCompatActivity {

    private int collId;
    private int subId = -1;

    /**
     * semester
     */
    @BindView(R.id.sub_semester_spin)
    Spinner semesterSpinner;
    private String semester = null;


    /**
     * branch
     */
    @BindView(R.id.sub_branch_spin)
    Spinner branchSpinner;
    private String branch = null;
    private SpinnerArrayAdapter branchAdapter;


    @BindView(R.id.sub_name_input)
    TextInputLayout sNameIn;

    @BindView(R.id.sub_full_name_input)
    TextInputLayout sFullNameIn;


    @OnClick(R.id.sub_edit_done_fab)
    void saveSubject() {

        String sName = Objects.requireNonNull(sNameIn.getEditText()).getText().toString().trim();
        String sFullName = Objects.requireNonNull(sFullNameIn.getEditText()).getText().toString().trim();

        Gson gson = new Gson();
        String subJson = gson.toJson(new Subject(semester, branch, sName, sFullName));

        VolleyTask.saveSubject(this, collId, subId, subJson);
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

        VolleyTask.setupBranchSpinner(this, collId, jObj -> {
            try {
                JSONArray brJsonArr = jObj.getJSONArray("branch_names");
                List<String> brList = new ArrayList<>();
                brList.add("Branch");
                for(int i=0; i<brJsonArr.length(); i++) {
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
                semester = (String) parent.getItemAtPosition(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
