package com.example.android.attendmanage;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    int collId;
    String facUserId = null;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        SharedPrefManager mSharedPref = SharedPrefManager.getInstance(this);
        if (mSharedPref.isLoggedIn()) {

            collId = mSharedPref.getCollId();
            String collName = mSharedPref.getCollName();
            String collFullName = mSharedPref.getCollFullName();
            String adminId = mSharedPref.getAdminId();

        } else {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
