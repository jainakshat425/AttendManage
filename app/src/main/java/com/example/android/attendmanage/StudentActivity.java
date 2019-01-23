package com.example.android.attendmanage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.android.attendmanage.adapter.StudentAdapter;
import com.example.android.attendmanage.editActivities.StudentEditActivity;
import com.example.android.attendmanage.pojos.Student;
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

public class StudentActivity extends AppCompatActivity {

    @BindView(R.id.student_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.student_empty_view)
    RelativeLayout emptyView;

    private StudentAdapter mAdapter;
    int collegeId;
    String semester;
    String branch;
    String section;
    Bundle bundle;

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

    @OnClick(R.id.student_add_fab)
    void addNewStudent() {
        Intent intent = new Intent(this, StudentEditActivity.class);
        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        ButterKnife.bind(this);

        collegeId = SharedPrefManager.getInstance(this).getCollId();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            semester = bundle.getString(ExtraUtils.EXTRA_SEMESTER);
            branch = bundle.getString(ExtraUtils.EXTRA_BRANCH);
            section = bundle.getString(ExtraUtils.EXTRA_SECTION);

            mAdapter = new StudentAdapter(this, new ArrayList<>());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(RecyclerView.VERTICAL);
            DividerItemDecoration divider = new DividerItemDecoration(this,
                    LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.addItemDecoration(divider);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void refreshList() {
        if (ExtraUtils.isNetworkAvailable(this)) {
            if (bundle != null) {
                VolleyTask.getStudents(this, collegeId, semester, branch, section, jObj -> {
                    ArrayList<Student> students = GsonUtils.extractStudentsFromJson(jObj);
                    mAdapter.swapList(students);

                    if (mAdapter.getItemCount() > 0) {
                        emptyView.setVisibility(View.GONE);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }

                });
            }
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
