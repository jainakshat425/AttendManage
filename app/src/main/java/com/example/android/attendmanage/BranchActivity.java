package com.example.android.attendmanage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.android.attendmanage.adapter.BranchAdapter;
import com.example.android.attendmanage.editActivities.BranchEditActivity;
import com.example.android.attendmanage.pojos.Branch;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.utilities.GsonUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;

public class BranchActivity extends AppCompatActivity {

    @BindView(R.id.branch_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.branch_empty_view)
    RelativeLayout emptyView;

    private BranchAdapter mAdapter;
    int collegeId;

    @OnClick(R.id.branch_add_fab)
    void addNewBranch() {
        Intent intent = new Intent(this, BranchEditActivity.class);
        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        ButterKnife.bind(this);

        collegeId = SharedPrefManager.getInstance(this).getCollId();

        mAdapter = new BranchAdapter(this, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        DividerItemDecoration divider = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setAdapter(mAdapter);

        refreshList();
    }

    private void refreshList() {
        VolleyTask.getBranches(this, collegeId, jObj -> {
            ArrayList<Branch> branches = GsonUtils.extractBranchesFromJson(jObj);
            if (branches != null && branches.size() > 0) {
                mAdapter.swapList(branches);
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
       refreshList();
    }
}
