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

import com.example.android.attendmanage.adapter.BranchAdapter;
import com.example.android.attendmanage.pojos.Branch;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.utilities.GsonUtils;
import com.example.android.attendmanage.volley.VolleyTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity {

    @BindView(R.id.branch_rv)
    RecyclerView mRecyclerView;

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

        VolleyTask.getBranches(this, collegeId, jObj -> {
            ArrayList<Branch> branches = GsonUtils.extractBranchesFromJson(jObj);
            mAdapter.swapList(branches);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        VolleyTask.getBranches(this, collegeId, jObj -> {
            ArrayList<Branch> branches = GsonUtils.extractBranchesFromJson(jObj);
            mAdapter.swapList(branches);
        });
    }
}
