package com.example.android.CollegeApp.editActivities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.android.CollegeApp.R;
import com.example.android.CollegeApp.SharedPrefManager;
import com.example.android.CollegeApp.adapter.SpinnerArrayAdapter;
import com.example.android.CollegeApp.pojos.Branch;
import com.example.android.CollegeApp.utilities.ExtraUtils;
import com.example.android.CollegeApp.volley.VolleyTask;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BranchEditActivity extends AppCompatActivity {

    private int collId;
    private int branchId = -1;

    @BindView(R.id.branch_hod_spin)
    Spinner hodSpinner;
    private SpinnerArrayAdapter hodAdapter;
    private String hodSelected = null;

    @BindView(R.id.branch_name_input)
    TextInputLayout bNameIn;
    String bName = "";


    @BindView(R.id.branch_full_name_input)
    TextInputLayout bFullNameIn;
    String bFullName = "";

    @OnClick(R.id.branch_edit_done_fab)
    void saveBranch() {

        bName = Objects.requireNonNull(bNameIn.getEditText()).getText().toString().trim();
        bFullName = Objects.requireNonNull(bFullNameIn.getEditText()).getText().toString().trim();

        if (validateInputs()) {

            Gson gson = new Gson();
            String branchJson = gson.toJson(new Branch(branchId, bName, bFullName, hodSelected));

            VolleyTask.saveBranch(this, collId, branchJson);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_edit);
        ButterKnife.bind(this);

        collId = SharedPrefManager.getInstance(this).getCollId();

        int mode = getIntent().getIntExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);

        if (mode == ExtraUtils.MODE_UPDATE) {

            Branch branch = getIntent().getParcelableExtra(ExtraUtils.EXTRA_BRANCH_OBJ);
            branchId = branch.getBranchId();

            Objects.requireNonNull(bNameIn.getEditText()).setText(branch.getBName());
            Objects.requireNonNull(bFullNameIn.getEditText()).setText(branch.getBFullName());
            hodSelected = branch.getHodId();
        }

        setupHodSpinner();
        refreshHodSpinner();
    }

    private void setupHodSpinner() {
        List<String> facList = new ArrayList<>();
        facList.add("Head of Department");
        String[] facArr = facList.toArray(new String[0]);
        hodAdapter = new SpinnerArrayAdapter(BranchEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                facArr);
        hodSpinner.setAdapter(hodAdapter);

        hodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0)
                    hodSelected = (String) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void refreshHodSpinner() {
        VolleyTask.getFacEmails(this, collId, jObj -> {
            try {
                List<String> facList = new ArrayList<>();
                facList.add("Head of Department");

                JSONArray facJsonArr = jObj.getJSONArray("fac_emails");

                for (int i = 0; i < facJsonArr.length(); i++) {
                    facList.add(facJsonArr.getString(i));
                }
                String[] facArr = facList.toArray(new String[0]);
                hodAdapter = new SpinnerArrayAdapter(BranchEditActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        facArr);
                hodSpinner.setAdapter(hodAdapter);

                if (!TextUtils.isEmpty(hodSelected)) {
                    for (int i = 0; i < facArr.length; i++) {
                        if (facArr[i].equals(hodSelected)) {
                            hodSpinner.setSelection(i);
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
        return isValidBranch();
    }

    private boolean isValidBranch() {
        if (TextUtils.isEmpty(bName)) {
            bNameIn.setError("Enter valid branch name.");
            return false;
        } else if (bName.length() > 8) {
            bNameIn.setError("Branch can contain atmost 8 characters.");
            return false;
        } else if (TextUtils.isEmpty(bFullName)) {
            bNameIn.setError(null);
            bFullNameIn.setError("Enter valid branch name.");
            return false;
        } else {
            bNameIn.setError(null);
            bFullNameIn.setError(null);
            return true;
        }
    }
}

