package com.example.android.CollegeApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.android.CollegeApp.adapter.FacultyAdapter;
import com.example.android.CollegeApp.editActivities.FacultyEditActivity;
import com.example.android.CollegeApp.pojos.Faculty;
import com.example.android.CollegeApp.utilities.ExtraUtils;
import com.example.android.CollegeApp.utilities.GsonUtils;
import com.example.android.CollegeApp.volley.VolleyTask;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FacultyActivity extends AppCompatActivity {

    @BindView(R.id.no_network_view)
    RelativeLayout noNetworkLayout;

    @BindView(R.id.faculty_empty_view)
    RelativeLayout emptyView;

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isNotConnected = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (isNotConnected) {
                noNetworkLayout.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                noNetworkLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                refreshList();
            }


        }
    };

    @BindView(R.id.fac_rv)
    RecyclerView mRecyclerView;


    private FacultyAdapter mAdapter;
    int collegeId;

    @OnClick(R.id.fac_add_fab)
    void addNewFaculty() {
        Intent intent = new Intent(this, FacultyEditActivity.class);
        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        ButterKnife.bind(this);

        collegeId = SharedPrefManager.getInstance(this).getCollId();

        mAdapter = new FacultyAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration divider = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void refreshList() {
        if (ExtraUtils.isNetworkAvailable(this)) {

            VolleyTask.getFaculties(this, collegeId, jObj -> {
                ArrayList<Faculty> faculties = GsonUtils.extractFacultiesFromJson(jObj);
                mAdapter.swapList(faculties);
                if (mAdapter.getItemCount() > 0) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        refreshList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkReceiver);
    }
}
