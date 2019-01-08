package com.example.android.attendmanage;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private SharedPrefManager mSharedPref;

    @OnClick(R.id.manage_branch_main)
    void startBranchActivity() {
        startActivity(new Intent(MainActivity.this, BranchActivity.class));
    }

    @OnClick(R.id.manage_class_main)
    void startClassActivity() {
        startActivity(new Intent(MainActivity.this, ClassActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mSharedPref = SharedPrefManager.getInstance(this);
        if (mSharedPref.isLoggedIn()) {

            int collId = mSharedPref.getCollId();
            String collName = mSharedPref.getCollName();
            String collFullName = mSharedPref.getCollFullName();
            String adminId = mSharedPref.getAdminId();

        } else {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
