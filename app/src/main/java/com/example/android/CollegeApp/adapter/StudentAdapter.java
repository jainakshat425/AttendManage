package com.example.android.CollegeApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.CollegeApp.R;
import com.example.android.CollegeApp.editActivities.StudentEditActivity;
import com.example.android.CollegeApp.pojos.Student;
import com.example.android.CollegeApp.utilities.ExtraUtils;
import com.example.android.CollegeApp.volley.VolleyTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 06-Jan-19.
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context mContext;
    private ArrayList<Student> mStudents;

    private boolean multiSelect = false;
    private ArrayList<Student> selectedItems = new ArrayList<>();

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
            List<Integer> stdIdList = new ArrayList<>();
            for (Student student : selectedItems) {

                mStudents.remove(student);
                stdIdList.add(student.getStudentId());

            }
            VolleyTask.deleteStudents(mContext, stdIdList, jObj -> notifyDataSetChanged());
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

    public StudentAdapter(Context context, ArrayList<Student> mStudents) {

        this.mContext = context;
        this.mStudents = mStudents;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.student_item, parent, false);

        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

        Student student = mStudents.get(position);

        String rollNo = student.getStdRollNo();
        String name = student.getStdName();

        holder.serialNoTv.setText(String.valueOf(position + 1));
        holder.stdNameTv.setText(name);
        holder.stdRollNoTv.setText(rollNo);

        holder.itemView.setTag(position);
        if (selectedItems.contains(student)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        if (mStudents == null)
            return 0;
        else
            return mStudents.size();
    }

    public void swapList(ArrayList<Student> students) {
        this.mStudents = students;
        this.notifyDataSetChanged();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener,View.OnLongClickListener {

        private TextView serialNoTv;
        private TextView stdNameTv;
        private TextView stdRollNoTv;

        StudentViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
            serialNoTv = view.findViewById(R.id.std_serial_no);
            stdNameTv = view.findViewById(R.id.std_name);
            stdRollNoTv = view.findViewById(R.id.std_roll_no);

        }
        void selectItem(Student item, View view) {
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
            Student student = mStudents.get(pos);

            if (currentActionMode != null) {

                selectItem(student, view);
            } else {

                Intent intent = new Intent(mContext, StudentEditActivity.class);
                intent.putExtra(ExtraUtils.EXTRA_STUDENT_OBJ, student);
                intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                mContext.startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View view) {

            if (currentActionMode != null) return false;

            currentActionMode = ((AppCompatActivity) mContext).startSupportActionMode(modeCallBack);
            int pos = (int) view.getTag();
            selectItem(mStudents.get(pos), view);

            return true;
        }

    }
}

