package com.example.android.attendmanage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.android.attendmanage.adapter.SubjectAdapter;
import com.example.android.attendmanage.editActivities.SubjectEditActivity;
import com.example.android.attendmanage.pojos.Subject;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.utilities.GsonUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectActivity extends AppCompatActivity {

    @BindView(R.id.sub_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.subject_empty_view)
    RelativeLayout emptyView;

    private SubjectAdapter mAdapter;
    int collegeId;

    @BindView(R.id.no_network_view)
    RelativeLayout noNetworkLayout;

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

    @OnClick(R.id.sub_add_fab)
    void addNewSubject() {
        Intent intent = new Intent(this, SubjectEditActivity.class);
        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        ButterKnife.bind(this);

        collegeId = SharedPrefManager.getInstance(this).getCollId();

        mAdapter = new SubjectAdapter(this, new ArrayList<>());
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

            VolleyTask.getSubjects(this, collegeId, jObj -> {
                ArrayList<Subject> subjects = GsonUtils.extractSubjectsFromJson(jObj);
                mAdapter.swapList(subjects);
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
