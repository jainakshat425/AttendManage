package com.example.android.attendmanage;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.root_layout)
    LinearLayout rootLayout;

    @BindView(R.id.reg_email_input)
    TextInputLayout emailIn;
    String email = "";

    @BindView(R.id.reg_pass_input)
    TextInputLayout passIn;
    String pass = "";

    @BindView(R.id.reg_pass_conf_input)
    TextInputLayout passConfIn;
    String passConfirm = "";

    @BindView(R.id.reg_coll_name_input)
    TextInputLayout collNameIn;
    String collName = "";

    @BindView(R.id.reg_coll_full_name_input)
    TextInputLayout collFullNameIn;
    String collFullName = "";

    @OnClick(R.id.reg_submit_button)
    void register() {

        email = Objects.requireNonNull(emailIn.getEditText()).getText().toString().trim();
        pass = Objects.requireNonNull(passIn.getEditText()).getText().toString().trim();
        passConfirm = Objects.requireNonNull(passConfIn.getEditText()).getText().toString().trim();
        collName = Objects.requireNonNull(collNameIn.getEditText()).getText().toString().trim();
        collFullName = Objects.requireNonNull(collFullNameIn.getEditText()).getText().toString().trim();

        if (ExtraUtils.isNetworkAvailable(this)) {
            if (validateInputs()) {

                VolleyTask.registerCollege(this, email, pass, collName, collFullName,
                        jObj -> {
                            finish();
                            startActivity(new Intent(this, LoginActivity.class));
                        });
            }
        } else {
            Snackbar.make(rootLayout, R.string.network_not_available, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }

    private boolean validateInputs() {
        if (!isValidEmail())
            return false;
        else if (!isValidPass())
            return false;
        else
            return isValidCollege();
    }

    private boolean isValidCollege() {
        if (TextUtils.isEmpty(collName) || collName.length() < 2) {
            collNameIn.setError("Enter valid College name.");
            return false;
        } else if (collName.length() > 8) {
            collNameIn.setError("Enter acronym of College name.");
            return false;
        } else if (TextUtils.isEmpty(collFullName) || collFullName.length() < 8) {
            collNameIn.setError(null);
            collFullNameIn.setError("Enter valid College name.");
            return false;
        } else if (collFullName.length() > 100) {
            collNameIn.setError(null);
            collFullNameIn.setError("College name too long.");
            return false;
        } else {
            collNameIn.setError(null);
            collFullNameIn.setError(null);
            return true;
        }
    }

    private boolean isValidPass() {
        if (TextUtils.isEmpty(pass) || pass.length() < 8) {
            passIn.setError("Password must contain minimum 8 characters.");
            return false;
        } else if (!passConfirm.equals(pass)) {
            passIn.setError(null);
            passConfIn.setError("Password doesn't matches.");
            return false;
        } else {
            passIn.setError(null);
            passConfIn.setError(null);
            return true;
        }
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
