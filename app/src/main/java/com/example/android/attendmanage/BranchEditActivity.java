package com.example.android.attendmanage;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.attendmanage.pojos.Branch;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyCallback;
import com.example.android.attendmanage.volley.VolleyTask;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BranchEditActivity extends AppCompatActivity {

    private int collId;
    private int branchId = -1;

    private SpinnerArrayAdapter hodAdapter;
    private String hodSelected = null;

    @BindView(R.id.branch_hod_spin)
    Spinner hodSpinner;

    @BindView(R.id.branch_name_input)
    TextInputLayout bNameIn;

    @BindView(R.id.branch_full_name_input)
    TextInputLayout bFullNameIn;

    @OnClick(R.id.branch_edit_done_fab)
    void saveBranch() {

        String bName = Objects.requireNonNull(bNameIn.getEditText()).getText().toString().trim();
        String bFullName = Objects.requireNonNull(bFullNameIn.getEditText()).getText().toString().trim();

        Gson gson = new Gson();
        String branchJson = gson.toJson(new Branch(bName, bFullName, hodSelected));

        VolleyTask.saveBranch(this, collId, branchId, branchJson);
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

        VolleyTask.setupHodSpinner(this, collId, jObj -> {
            try {
                JSONArray facJsonArr = jObj.getJSONArray("faculty");
                List<String> facList = new ArrayList<>();
                facList.add("Head Of Dept.");
                for(int i=0; i<facJsonArr.length(); i++) {
                    facList.add(facJsonArr.getString(i));
                }
                String[] facArr = facList.toArray(new String[0]);
                hodAdapter = new SpinnerArrayAdapter(BranchEditActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        facArr);
                hodSpinner.setAdapter(hodAdapter);

                if (hodSelected != null) {
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

        hodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                hodSelected = (String) parent.getItemAtPosition(pos);
                Toast.makeText(BranchEditActivity.this, hodSelected, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
