package com.example.android.attendmanage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.pojos.Faculty;
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

public class FacultyEditActivity extends AppCompatActivity {

    private int collId;
    private int facId = -1;

    /**
     * Department
     */
    @BindView(R.id.fac_dept_spin)
    Spinner deptSpinner;
    private String dept = null;
    private SpinnerArrayAdapter deptAdapter;

    @BindView(R.id.fac_name_input)
    TextInputLayout nameIn;

    @BindView(R.id.fac_user_id_input)
    TextInputLayout emailIn;

    @BindView(R.id.fac_mob_no_input)
    TextInputLayout mobNoIn;

    @OnClick(R.id.fac_edit_done_fab)
    void saveFaculty() {

        String email = Objects.requireNonNull(emailIn.getEditText()).getText().toString().trim();
        String name = Objects.requireNonNull(nameIn.getEditText()).getText().toString().trim();
        String mobNo = Objects.requireNonNull(mobNoIn.getEditText()).getText().toString().trim();

        Gson gson = new Gson();
        String facJson = gson.toJson(new Faculty(facId, email, name, mobNo, dept));

        VolleyTask.saveFaculty(this, collId, facJson);
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

            Objects.requireNonNull(emailIn.getEditText()).setText(faculty.getFacUserId());
            Objects.requireNonNull(nameIn.getEditText()).setText(faculty.getFacName());
            Objects.requireNonNull(mobNoIn.getEditText()).setText(faculty.getMobNo());

            dept = faculty.getDeptName();
        }

        VolleyTask.setupBranchSpinner(this, collId, jObj -> {
            try {
                JSONArray brJsonArr = jObj.getJSONArray("branch_names");
                List<String> brList = new ArrayList<>();
                brList.add("Department");
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
}
