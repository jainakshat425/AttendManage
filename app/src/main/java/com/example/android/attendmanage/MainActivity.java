package com.example.android.attendmanage;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private SharedPrefManager mSharedPref;

    @BindView(R.id.manage_branch_main)
    RelativeLayout branchLayout;

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

            branchLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, BranchActivity.class));
                }
            });
            Toast.makeText(this, collFullName +" : "+ collId, Toast.LENGTH_SHORT).show();
        } else {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
