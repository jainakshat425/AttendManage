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

import com.example.android.attendmanage.adapter.ClassAdapter;
import com.example.android.attendmanage.editActivities.ClassEditActivity;
import com.example.android.attendmanage.pojos.Class;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.utilities.GsonUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;

public class ClassActivity extends AppCompatActivity {

    @BindView(R.id.class_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.class_empty_view)
    RelativeLayout emptyView;

    private ClassAdapter mAdapter;
    int collegeId;

    @OnClick(R.id.class_add_fab)
    void addNewClass() {
        Intent intent = new Intent(this, ClassEditActivity.class);
        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        ButterKnife.bind(this);

        collegeId = SharedPrefManager.getInstance(this).getCollId();

        mAdapter = new ClassAdapter(this, new ArrayList<>());
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
        VolleyTask.getClasses(this, collegeId, jObj -> {
            ArrayList<Class> classes = GsonUtils.extractClassesFromJson(jObj);
            mAdapter.swapList(classes);
            if (mAdapter.getItemCount() > 0) {
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
