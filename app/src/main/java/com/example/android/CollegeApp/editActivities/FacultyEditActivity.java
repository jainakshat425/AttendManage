package com.example.android.CollegeApp.editActivities;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.android.CollegeApp.R;
import com.example.android.CollegeApp.SharedPrefManager;
import com.example.android.CollegeApp.adapter.SpinnerArrayAdapter;
import com.example.android.CollegeApp.pojos.Faculty;
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

public class FacultyEditActivity extends AppCompatActivity {

    private int collId;
    private int facId = -1;

    @BindView(R.id.fac_edit_layout)
    LinearLayout layout;

    /**
     * Department
     */
    @BindView(R.id.fac_dept_spin)
    Spinner deptSpinner;
    private String dept = "";
    private SpinnerArrayAdapter deptAdapter;

    @BindView(R.id.fac_name_input)
    TextInputLayout nameIn;
    private String name = "";

    @BindView(R.id.fac_user_id_input)
    TextInputLayout emailIn;
    private String email = "";

    @BindView(R.id.fac_mob_no_input)
    TextInputLayout mobNoIn;
    private String mobNo = "";

    @OnClick(R.id.fac_edit_done_fab)
    void saveFaculty() {

        email = Objects.requireNonNull(emailIn.getEditText()).getText().toString().trim();
        name = Objects.requireNonNull(nameIn.getEditText()).getText().toString().trim();
        mobNo = Objects.requireNonNull(mobNoIn.getEditText()).getText().toString().trim();

        if (validateInputs()) {
            Gson gson = new Gson();
            String facJson = gson.toJson(new Faculty(facId, email, name, mobNo, dept));

            VolleyTask.saveFaculty(this, collId, facJson);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_edit);
        ButterKnife.bind(this);

        collId = SharedPrefManager.getInstance(this).getCollId();

        int mode = getIntent().getIntExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);

        if (mode == ExtraUtils.MODE_UPDATE) {

            Faculty faculty = getIntent().getParcelableExtra(ExtraUtils.EXTRA_FACULTY_OBJ);
            facId = faculty.getFacId();

            Objects.requireNonNull(emailIn.getEditText()).setText(faculty.getfacEmail());
            Objects.requireNonNull(nameIn.getEditText()).setText(faculty.getFacName());
            Objects.requireNonNull(mobNoIn.getEditText()).setText(faculty.getMobNo());

            dept = faculty.getDeptName();
        }

        setupDeptSpinner();
        refreshDeptSpinner();
    }

    private void refreshDeptSpinner() {
        List<String> brList = new ArrayList<>();
        brList.add("Select");
        VolleyTask.getBranchNames(this, collId, jObj -> {
            try {
                JSONArray brJsonArr = jObj.getJSONArray("branch_names");

                for (int i = 0; i < brJsonArr.length(); i++) {
                    brList.add(brJsonArr.getString(i));
                }
                String[] brArr = brList.toArray(new String[0]);
                deptAdapter = new SpinnerArrayAdapter(FacultyEditActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        brArr);
                deptSpinner.setAdapter(deptAdapter);

                if (dept != null) {
                    for (int i = 0; i < brArr.length; i++) {
                        if (brArr[i].equals(dept)) {
                            deptSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupDeptSpinner() {
        List<String> brList = new ArrayList<>();
        brList.add("Select");
        String[] brArr = brList.toArray(new String[0]);
        deptAdapter = new SpinnerArrayAdapter(FacultyEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                brArr);
        deptSpinner.setAdapter(deptAdapter);

        deptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0) {
                    dept = (String) parent.getItemAtPosition(pos);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private boolean validateInputs() {
        if (!isValidEmail()) {
            return false;
        } else if (TextUtils.isEmpty(dept)) {
            Snackbar.make(layout, "Department not selected!", Snackbar.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(name) || name.length() < 3) {
            nameIn.setError("Enter valid name.");
            return false;
        } else if (TextUtils.isEmpty(mobNo) || mobNo.length() != 10) {
            nameIn.setError(null);
            mobNoIn.setError("Enter valid 10-digit mobile number.");
            return false;
        } else {
            nameIn.setError(null);
            mobNoIn.setError(null);
        }
        return true;
    }

    private boolean isValidEmail() {
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailIn.setError("Enter valid Email Address.");
            return false;
        } else {
            emailIn.setError(null);
            return true;
        }
    }

}
