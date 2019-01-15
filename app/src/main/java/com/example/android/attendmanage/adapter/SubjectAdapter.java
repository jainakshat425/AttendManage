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

import com.example.android.attendmanage.R;
import com.example.android.attendmanage.SubjectEditActivity;
import com.example.android.attendmanage.pojos.Subject;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 06-Jan-19.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private Context mContext;
    private ArrayList<Subject> mSubjects;

    private boolean multiSelect = false;
    private ArrayList<Subject> selectedItems = new ArrayList<>();

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
            List<Integer> subIdList = new ArrayList<>();
            for (Subject subject : selectedItems) {

                mSubjects.remove(subject);
                subIdList.add(subject.getSubId());

            }
            VolleyTask.deleteSubjects(mContext, subIdList, jObj -> notifyDataSetChanged());
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

    public SubjectAdapter(Context context, ArrayList<Subject> subjects) {

        this.mContext = context;
        this.mSubjects = subjects;
    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.subject_item, parent, false);

        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {

        Subject subject = mSubjects.get(position);

        String bName = subject.getbName();
        String semester = subject.getSubSemester();
        String sFullName = subject.getSubFullName();
        String sName = subject.getSubName();

        holder.bNameTv.setText(bName);
        holder.subFullNameTv.setText(String.format("%s (%s)", sFullName, sName));

        holder.semesterTv.setText(ExtraUtils.getSemester(semester));

        holder.itemView.setTag(position);
        if (selectedItems.contains(subject)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        if (mSubjects == null)
            return 0;
        else
            return mSubjects.size();
    }

    public void swapList(ArrayList<Subject> subjects) {
        this.mSubjects = subjects;
        this.notifyDataSetChanged();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private TextView bNameTv;
        private TextView subFullNameTv;
        private TextView semesterTv;

        SubjectViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
            bNameTv = view.findViewById(R.id.sub_b_name);
            subFullNameTv = view.findViewById(R.id.sub_name);
            semesterTv = view.findViewById(R.id.sub_semester);

        }

        void selectItem(Subject item, View view) {
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
            Subject subject = mSubjects.get(pos);

            if (currentActionMode != null) {
                selectItem(subject, view);
                if (subFullNameTv.isSelected()) subFullNameTv.setSelected(false);
                else subFullNameTv.setSelected(true);
            } else {

                Intent intent = new Intent(mContext, SubjectEditActivity.class);
                intent.putExtra(ExtraUtils.EXTRA_SUBJECT_OBJ, subject);
                intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                mContext.startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View view) {

            if (currentActionMode != null) return false;

            currentActionMode = ((AppCompatActivity) mContext).startSupportActionMode(modeCallBack);
            int pos = (int) view.getTag();
            selectItem(mSubjects.get(pos), view);

            return true;
        }

    }
}

