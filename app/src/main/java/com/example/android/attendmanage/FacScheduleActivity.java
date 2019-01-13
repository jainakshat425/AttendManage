package com.example.android.attendmanage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.attendmanage.adapter.FacSchAdapter;
import com.example.android.attendmanage.pojos.FacSchedule;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.utilities.GsonUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.antonious.materialdaypicker.MaterialDayPicker;
import ca.antonious.materialdaypicker.SingleSelectionMode;

public class FacScheduleActivity extends AppCompatActivity {

    @BindView(R.id.fac_sch_rv)
    RecyclerView mRecyclerView;

    private FacSchAdapter mAdapter;
    ArrayList<FacSchedule> mFacSch;

    int collegeId;
    String facUserId;
    String day;

    @OnClick(R.id.fac_sch_add_fab)
    void addNewFacSch() {
        Intent intent = new Intent(this, FacSchEditActivity.class);
        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_sch);
        ButterKnife.bind(this);

        collegeId = SharedPrefManager.getInstance(this).getCollId();
        facUserId = getIntent().getStringExtra(ExtraUtils.EXTRA_FAC_USER_ID);
        day = ExtraUtils.getCurrentDay();

        final MaterialDayPicker materialDayPicker = findViewById(R.id.fac_sch_day_pick);
        materialDayPicker.setSelectionMode(SingleSelectionMode.create());
        materialDayPicker.setBackgroundColor(Color.WHITE);
        materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.valueOf(day));

        materialDayPicker.setDayPressedListener((weekday, isSelected) -> {
            if (isSelected) {
                day = weekday.toString();
                refreshList();
            }
        });

        mFacSch = new ArrayList<>();
        mAdapter = new FacSchAdapter(this, mFacSch);
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
        VolleyTask.getFacSchedule(this, facUserId, day, jObj -> {
            mFacSch = GsonUtils.extractFacSchFromJson(jObj);
            mAdapter.swapList(mFacSch);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

}
