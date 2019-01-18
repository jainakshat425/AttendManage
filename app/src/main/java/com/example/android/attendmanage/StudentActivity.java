package com.example.android.attendmanage;

import android.content.Intent;
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

            refreshList();
        }

    }

    private void refreshList() {
        VolleyTask.getStudents(this, collegeId, semester, branch, section, jObj -> {
            ArrayList<Student> students = GsonUtils.extractStudentsFromJson(jObj);
            if (students != null && students.size() > 0) {
                mAdapter.swapList(students);
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bundle != null)
            refreshList();
    }
}
