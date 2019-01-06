package com.example.android.attendmanage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPrefManager mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPref = SharedPrefManager.getInstance(this);
        if (mSharedPref.isLoggedIn()) {

            int collId = mSharedPref.getCollId();
            String collName = mSharedPref.getCollName();
            String collFullName = mSharedPref.getCollFullName();
            String adminId = mSharedPref.getAdminId();

            Toast.makeText(this, collFullName +" : "+ collId, Toast.LENGTH_SHORT).show();
        } else {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
