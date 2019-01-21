package com.example.android.attendmanage;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private int collId;
    private SharedPrefManager mSharedPref;
    private String facUserId = null;

    @OnClick(R.id.manage_branch_main)
    void startBranchActivity() {
        startActivity(new Intent(MainActivity.this, BranchActivity.class));
    }

    @OnClick(R.id.manage_class_main)
    void startClassActivity() {
        startActivity(new Intent(MainActivity.this, ClassActivity.class));
    }

    @OnClick(R.id.manage_sub_main)
    void startSubjectActivity() {
        startActivity(new Intent(MainActivity.this, SubjectActivity.class));
    }

    @OnClick(R.id.manage_student_main)
    void startClassSelectActivity() {
        startActivity(new Intent(MainActivity.this, ClassSelectActivity.class));
    }

    @OnClick(R.id.manage_fac_main)
    void startFacultyActivity() {
        startActivity(new Intent(MainActivity.this, FacultyActivity.class));
    }

    @OnClick(R.id.manage_fac_sch_main)
    void startFacSchManageActivity() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select faculty");

        View dialogView = View.inflate(this, R.layout.spinner_dialog_layout, null);

        final Spinner facSpinner = dialogView.findViewById(R.id.dialog_fac_id_spin);

        VolleyTask.getFacUserIds(this, collId, jObj -> {
            try {
                JSONArray facIdJsonArr = jObj.getJSONArray("fac_user_ids");
                List<String> facIdList = new ArrayList<>();
                facIdList.add("Select");

                for (int i = 0; i < facIdJsonArr.length(); i++) {
                    facIdList.add(facIdJsonArr.getString(i));
                }

                String[] facIdArr = facIdList.toArray(new String[0]);
                SpinnerArrayAdapter facIdAdapter = new SpinnerArrayAdapter(MainActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        facIdArr);
                facSpinner.setAdapter(facIdAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        facSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0)
                    facUserId = (String) parent.getItemAtPosition(pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(dialogView);
        builder.setPositiveButton("Next", (dialog, which) -> {
            if (facUserId != null) {
                Intent intent = new Intent(MainActivity.this, FacScheduleActivity.class);
                intent.putExtra(ExtraUtils.EXTRA_FAC_USER_ID, facUserId);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @BindView(R.id.main_coll_full_name)
    TextView collFullNameTv;

    @BindView(R.id.main_coll_name)
    TextView collNameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        checkLoggedIn();
    }

    private void checkLoggedIn() {
        mSharedPref = SharedPrefManager.getInstance(this);
        if (mSharedPref.isLoggedIn()) {

            collId = mSharedPref.getCollId();
            String adminId = mSharedPref.getCollEmail();

            collNameTv.setText(mSharedPref.getCollName());
            collFullNameTv.setText(mSharedPref.getCollFullName());
        } else {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.main_settings:
                showSettingSelectDialog();
                break;
        }

        return true;
    }

    private void showSettingSelectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = View.inflate(this, R.layout.setting_select_dialog_layout, null);

        builder.setView(dialogView);

        final AlertDialog dialog = builder.show();

        dialogView.findViewById(R.id.edit_profile).setOnClickListener(view -> {
            showEditProfileDialog();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.change_password).setOnClickListener(view -> {
            showChangePasswordDialog();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.logout).setOnClickListener(view -> {
            logout();
            dialog.dismiss();
        });
    }

    private void logout() {
        mSharedPref.clearCredentials();
        checkLoggedIn();
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = View.inflate(this, R.layout.change_password_layout, null);

        TextInputLayout currentPassIn = dialogView.findViewById(R.id.current_pass_input);
        TextInputLayout newPassIn = dialogView.findViewById(R.id.new_pass_input);
        TextInputLayout confirmPassIn = dialogView.findViewById(R.id.confirm_pass_input);

        builder.setView(dialogView);
        final AlertDialog dialog = builder.show();

        dialogView.findViewById(R.id.cp_modify_button).setOnClickListener(view -> {
            if (currentPassIn.getEditText() != null
                    && newPassIn.getEditText() != null
                    && confirmPassIn.getEditText() != null) {

                String currentPass = currentPassIn.getEditText().getText().toString().trim();
                String newPass = newPassIn.getEditText().getText().toString().trim();
                String confirmPass = confirmPassIn.getEditText().getText().toString().trim();

                boolean valid = validatePassInputs(currentPass, newPass, confirmPass,
                        currentPassIn, newPassIn, confirmPassIn);

                if (valid) {
                    VolleyTask.changeCollegePassword(MainActivity.this, collId, currentPass, newPass,
                            jObj -> dialog.dismiss());
                }
            }
        });

        dialogView.findViewById(R.id.cp_cancel_button).setOnClickListener(view ->
                dialog.dismiss());
    }

    private void showEditProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View dialogView = View.inflate(this, R.layout.college_edit_dialog_layout, null);

        TextInputLayout passIn = dialogView.findViewById(R.id.coll_pass_input);
        TextInputLayout collNameIn = dialogView.findViewById(R.id.coll_name_input);
        TextInputLayout collFullNameIn = dialogView.findViewById(R.id.coll_full_name_input);
        TextInputLayout emailIn = dialogView.findViewById(R.id.coll_admin_id_input);

        if (collNameIn.getEditText() != null
                && collFullNameIn.getEditText() != null
                && emailIn.getEditText() != null) {

            collNameIn.getEditText().setText(mSharedPref.getCollName());
            collFullNameIn.getEditText().setText(mSharedPref.getCollFullName());
            emailIn.getEditText().setText(mSharedPref.getCollEmail());
        }

        builder.setView(dialogView);

        final AlertDialog dialog = builder.show();

        dialogView.findViewById(R.id.ep_update_button).setOnClickListener(view -> {

            if (passIn.getEditText() != null
                    && collNameIn.getEditText() != null
                    && collFullNameIn.getEditText() != null
                    && emailIn.getEditText() != null) {

                String password = passIn.getEditText().getText().toString().trim();
                String collName = collNameIn.getEditText().getText().toString().trim();
                String collFullName = collFullNameIn.getEditText().getText().toString().trim();
                String email = emailIn.getEditText().getText().toString().trim();

                boolean valid = validateCollegeDetailInputs(password, collName, collFullName,
                        email, passIn, collNameIn, collFullNameIn, emailIn);

                if (valid) {
                    VolleyTask.updateCollegeDetails(this, collId, password, collName, collFullName,
                            email, jObj -> {
                                dialog.dismiss();
                                mSharedPref.saveCollegeDetails(collId, collName, collFullName, email);
                                collNameTv.setText(collName);
                                collFullNameTv.setText(collFullName);
                            });
                }
            }
        });

        dialogView.findViewById(R.id.ep_cancel_button).setOnClickListener(view -> dialog.dismiss());
    }

    private boolean validatePassInputs(String currentPass, String newPass, String confirmPass,
                                       TextInputLayout currentPassIn, TextInputLayout newPassIn,
                                       TextInputLayout confirmPassIn) {

        if (TextUtils.isEmpty(currentPass) || currentPass.length() < 8) {
            currentPassIn.setError("Password must contain atleast 8-characters.");
            return false;

        } else if (TextUtils.isEmpty(newPass) || newPass.length() < 8) {
            currentPassIn.setError(null);
            newPassIn.setError("Password must contain atleast 8-characters.");
            return false;

        } else if (TextUtils.isEmpty(confirmPass) || confirmPass.length() < 8) {
            currentPassIn.setError(null);
            newPassIn.setError(null);
            confirmPassIn.setError("Password must contain atleast 8-characters.");
            return false;

        } else if (!newPass.equals(confirmPass)) {
            currentPassIn.setError(null);
            newPassIn.setError(null);
            confirmPassIn.setError("Password doesn't match.");
            return false;

        } else {
            currentPassIn.setError(null);
            newPassIn.setError(null);
            confirmPassIn.setError(null);
            return true;
        }
    }

    private boolean validateCollegeDetailInputs(String password, String collName,
                                                String collFullName, String email,
                                                TextInputLayout passIn, TextInputLayout collNameIn,
                                                TextInputLayout collFullNameIn, TextInputLayout emailIn) {
        if (TextUtils.isEmpty(password) || password.length() < 8) {
            passIn.setError("Password must contain atleast 8-characters.");
            return false;

        } else passIn.setError(null);

        if (TextUtils.isEmpty(collName) || collName.length() < 2) {
            collNameIn.setError("Enter valid College name.");
            return false;

        } else if (collName.length() > 8) {
            collNameIn.setError("Enter acronym of College name.");
            return false;

        } else collNameIn.setError(null);

        if (TextUtils.isEmpty(collFullName) || collFullName.length() < 8) {
            collFullNameIn.setError("Enter valid College name.");
            return false;

        } else if (collFullName.length() > 100) {
            collFullNameIn.setError("College name too long.");
            return false;

        } else collFullNameIn.setError(null);

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailIn.setError("Enter valid Email Address.");
            return false;
        } else emailIn.setError(null);

        return true;
    }

}
