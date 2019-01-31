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

import com.example.android.CollegeApp.editActivities.ClassEditActivity;
import com.example.android.CollegeApp.R;
import com.example.android.CollegeApp.pojos.Class;
import com.example.android.CollegeApp.utilities.ExtraUtils;
import com.example.android.CollegeApp.volley.VolleyTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 08-Jan-19.
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private Context mContext;
    private ArrayList<Class> mClasses;

    private boolean multiSelect = false;
    private ArrayList<Class> selectedItems = new ArrayList<>();

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
            List<Integer> classIdList = new ArrayList<>();
            for (Class _class : selectedItems) {

                mClasses.remove(_class);
                classIdList.add(_class.getClassId());

            }
            VolleyTask.deleteClasses(mContext, classIdList, jObj -> notifyDataSetChanged());
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

    public ClassAdapter(Context context, ArrayList<Class> classes) {

        this.mContext = context;
        this.mClasses = classes;
    }

    @NonNull
    @Override
    public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.class_item, parent, false);

        return new ClassViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {

        Class myClass = mClasses.get(position);

        String bName = myClass.getBranchName();
        String semester = String.valueOf(myClass.getSemester());
        String section = myClass.getSection();

        holder.bNameTv.setText(bName);
        holder.semesterTv.setText(ExtraUtils.getSemester(semester));
        if (section != null)
            holder.sectionTv.setText(String.format("Section: %s", section));

        holder.itemView.setTag(position);
        if (selectedItems.contains(myClass)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        if (mClasses == null)
            return 0;
        else
            return mClasses.size();
    }

    public void swapList(ArrayList<Class> classes) {
        this.mClasses = classes;
        this.notifyDataSetChanged();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private TextView bNameTv;
        private TextView semesterTv;
        private TextView sectionTv;

        ClassViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
            bNameTv = view.findViewById(R.id.class_b_name);
            semesterTv = view.findViewById(R.id.class_semester);
            sectionTv = view.findViewById(R.id.class_section);

        }

        void selectItem(Class item, View view) {
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
            Class _class = mClasses.get(pos);

            if (currentActionMode != null) {

                selectItem(_class, view);
            } else {

                Intent intent = new Intent(mContext, ClassEditActivity.class);
                intent.putExtra(ExtraUtils.EXTRA_CLASS_OBJ, _class);
                intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                mContext.startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View view) {

            if (currentActionMode != null) return false;

            currentActionMode = ((AppCompatActivity) mContext).startSupportActionMode(modeCallBack);
            int pos = (int) view.getTag();
            selectItem(mClasses.get(pos), view);

            return true;
        }
    }
}

