package com.example.android.attendmanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.attendmanage.FacSchEditActivity;
import com.example.android.attendmanage.R;
import com.example.android.attendmanage.pojos.FacSchedule;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 06-Jan-19.
 */
public class FacSchAdapter extends RecyclerView.Adapter<FacSchAdapter.FacSchHolder> {

    private Context mContext;
    private List<FacSchedule> mFacSch;

    private boolean multiSelect = false;
    private ArrayList<FacSchedule> selectedItems = new ArrayList<>();

    // Tracks current contextual action mode
    private ActionMode currentActionMode;
    // Define the callback when ActionMode is activated
    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            multiSelect = true;
            mode.setTitle("Actions");
            mode.getMenuInflater().inflate(R.menu.action_mode, menu);
            return true;
        }

        // Called each time the action mode is shown.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            List<Integer> lectIdList = new ArrayList<>();
            for (FacSchedule schItem : selectedItems) {

                mFacSch.remove(schItem);
                lectIdList.add(schItem.getLectId());

            }
            VolleyTask.deleteFacSchs(mContext, lectIdList, jObj -> {
                notifyDataSetChanged();
            });
            mode.finish();
            return true;
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null; // Clear current action mode
            multiSelect = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };

    public FacSchAdapter(Context context, List<FacSchedule> mFacSch) {
        this.mContext = context;
        this.mFacSch = mFacSch;
    }

    @NonNull
    @Override
    public FacSchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.fac_sch_item, parent, false);
        return new FacSchHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacSchHolder holder, int position) {

        FacSchedule sch = mFacSch.get(position);

        String semester = String.valueOf(sch.getSem());
        String branch = sch.getBName();
        String section = sch.getSection();
        String lectNo = String.valueOf(sch.getLectNo());
        String subName = sch.getSubName();
        String lectStartTime = String.valueOf(sch.getLectStartTime());
        String lectEndTime = String.valueOf(sch.getLectEndTime());

        try {
            Date lectStartTimeDisplay = ExtraUtils.timeFormat.parse(lectStartTime);
            lectStartTime = ExtraUtils.timeDisplayFormat.format(lectStartTimeDisplay);
            Date lectEndTimeDisplay = ExtraUtils.timeFormat.parse(lectEndTime);
            lectEndTime = ExtraUtils.timeDisplayFormat.format(lectEndTimeDisplay);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.lectNoTv.setText(lectNo);
        holder.semesterTv.setText(ExtraUtils.getSemester(semester));
        holder.branchTv.setText(branch);
        holder.sectionTv.setText(section);
        holder.subjectNameTv.setText(String.format("Subject: %s", subName));
        holder.lectStartTimeTv.setText(lectStartTime);
        holder.lectEndTimeTv.setText(lectEndTime);

        holder.itemView.setTag(position);
        if (selectedItems.contains(sch)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

    }

    public class FacSchHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        final TextView lectNoTv;
        final TextView semesterTv;
        final TextView branchTv;
        final TextView sectionTv;
        final TextView subjectNameTv;
        final TextView lectStartTimeTv;
        final TextView lectEndTimeTv;

        public FacSchHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            lectNoTv = view.findViewById(R.id.fac_sch_lect_no_tv);
            semesterTv = view.findViewById(R.id.fac_sch_semester_tv);
            branchTv = view.findViewById(R.id.fac_sch_branch_tv);
            sectionTv = view.findViewById(R.id.fac_sch_section_tv);
            subjectNameTv = view.findViewById(R.id.fac_sch_subject_tv);
            lectStartTimeTv = view.findViewById(R.id.fac_sch_lect_start_tv);
            lectEndTimeTv = view.findViewById(R.id.fac_sch_lect_end_tv);
        }

        void selectItem(FacSchedule item, View view) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item);
                    view.setBackgroundColor(Color.WHITE);
                } else {
                    selectedItems.add(item);
                    view.setBackgroundColor(Color.LTGRAY);
                }
            }
        }


        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();
            FacSchedule facSch = mFacSch.get(pos);

            if (currentActionMode != null) {

                selectItem(facSch, view);
            } else {

                Intent intent = new Intent(mContext, FacSchEditActivity.class);
                intent.putExtra(ExtraUtils.EXTRA_FAC_SCH_OBJ, facSch);
                intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                mContext.startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View view) {

            if (currentActionMode != null) return false;

            currentActionMode = ((AppCompatActivity) mContext).startSupportActionMode(modeCallBack);
            int pos = (int) view.getTag();
            selectItem(mFacSch.get(pos), view);

            return true;
        }
    }

    public void swapList(List<FacSchedule> mFacSch) {
        this.mFacSch = mFacSch;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mFacSch != null) return mFacSch.size();
        else return 0;
    }

}
