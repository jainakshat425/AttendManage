package com.example.android.attendmanage;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ClassSelectActivity extends AppCompatActivity {

    /**
     * semester
     */
    @BindView(R.id.class_select_semester_spin)
    Spinner semesterSpinner;
    private String semester = null;

    /**
     * branch
     */
    @BindView(R.id.class_select_branch_spin)
    Spinner branchSpinner;
    private String branch = null;
    private SpinnerArrayAdapter branchAdapter;

    /**
     * section
     */
    @BindView(R.id.class_select_section_spin)
    Spinner sectionSpinner;
    private String section = null;

    @OnClick(R.id.fab_show_students)
    void showStudents() {
        if (semester != null && branch != null && section != null) {
            Intent intent = new Intent(this, StudentActivity.class);
            intent.putExtra(ExtraUtils.EXTRA_BRANCH, branch);
            intent.putExtra(ExtraUtils.EXTRA_SEMESTER, semester);
            intent.putExtra(ExtraUtils.EXTRA_SECTION, section);
            startActivity(intent);
        } else
            Toast.makeText(this, "Inputs Missing", Toast.LENGTH_SHORT).show();
    }

    int collegeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_select);
        ButterKnife.bind(this);

        collegeId = SharedPrefManager.getInstance(this).getCollId();

        VolleyTask.setupBranchSpinner(this, collegeId, jObj -> {
            try {
                JSONArray brJsonArr = jObj.getJSONArray("branch_names");
                List<String> brList = new ArrayList<>();
                brList.add("Branch");
                for (int i = 0; i < brJsonArr.length(); i++) {
                    brList.add(brJsonArr.getString(i));
                }
                String[] brArr = brList.toArray(new String[0]);
                branchAdapter = new SpinnerArrayAdapter(ClassSelectActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        brArr);
                branchSpinner.setAdapter(branchAdapter);

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
            final List<String> secArr = new ArrayList<>();
            secArr.add("Section");
            VolleyTask.getSections(ClassSelectActivity.this, branch,
                    semester, collegeId, jObj -> {

                        try {
                            JSONArray jsonArray = jObj.getJSONArray("sections");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                secArr.add(jsonArray.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SpinnerArrayAdapter sectionAdapter =
                                new SpinnerArrayAdapter(ClassSelectActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        secArr.toArray(new String[0]));
                        sectionSpinner.setAdapter(sectionAdapter);

                    });
        } else
            ExtraUtils.emptySectionSpinner(this, sectionSpinner);
    }

    private void setupSemesterSpinner() {
        String[] semArr = getResources().getStringArray(R.array.semester_array);
        SpinnerArrayAdapter semesterAdapter = new SpinnerArrayAdapter(ClassSelectActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                semArr);
        semesterSpinner.setAdapter(semesterAdapter);
    }
}
