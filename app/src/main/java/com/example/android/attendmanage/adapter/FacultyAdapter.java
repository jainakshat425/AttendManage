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

import com.example.android.attendmanage.editActivities.FacultyEditActivity;
import com.example.android.attendmanage.R;
import com.example.android.attendmanage.pojos.Faculty;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 14-Jan-19.
 */
public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.FacultyViewHolder> {

    private Context mContext;
    private ArrayList<Faculty> mFaculty;

    private boolean multiSelect = false;
    private ArrayList<Faculty> selectedItems = new ArrayList<>();

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
            List<Integer> facIdList = new ArrayList<>();
            for (Faculty fac : selectedItems) {

                mFaculty.remove(fac);
                facIdList.add(fac.getFacId());

            }
            VolleyTask.deleteFaculties(mContext, facIdList, jObj -> {
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

    public FacultyAdapter(Context context, ArrayList<Faculty> faculties) {

        this.mContext = context;
        this.mFaculty = faculties;
    }

    @NonNull
    @Override
    public FacultyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.faculty_item, parent, false);

        return new FacultyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacultyViewHolder holder, int position) {

        Faculty faculty = mFaculty.get(position);

        String facUserId = faculty.getFacUserId();
        String mobNo = faculty.getMobNo();
        String dept = faculty.getDeptName();
        String name = faculty.getFacName();
        int facId = faculty.getFacId();

        holder.nameTv.setText(name);
        holder.facUserIdTv.setText(facUserId);
        holder.deptTv.setText(dept);
        holder.mobNoTv.setText(mobNo);

        holder.itemView.setTag(position);
        if (selectedItems.contains(faculty)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        if (mFaculty == null)
            return 0;
        else
            return mFaculty.size();
    }

    public void swapList(ArrayList<Faculty> faculties) {
        this.mFaculty = faculties;
        this.notifyDataSetChanged();
    }

    public class FacultyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        private TextView nameTv;
        private TextView facUserIdTv;
        private TextView deptTv;
        private TextView mobNoTv;

        FacultyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            nameTv = view.findViewById(R.id.fac_name);
            facUserIdTv = view.findViewById(R.id.fac_user_id);
            deptTv = view.findViewById(R.id.fac_dept);
            mobNoTv = view.findViewById(R.id.fac_mob_no);
        }

        void selectItem(Faculty item, View view) {
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
            Faculty faculty = mFaculty.get(pos);

            if (currentActionMode != null) {

                selectItem(faculty, view);
            } else {

                Intent intent = new Intent(mContext, FacultyEditActivity.class);
                intent.putExtra(ExtraUtils.EXTRA_FACULTY_OBJ, faculty);
                intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                mContext.startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View view) {

            if (currentActionMode != null) return false;

            currentActionMode = ((AppCompatActivity) mContext).startSupportActionMode(modeCallBack);
            int pos = (int) view.getTag();
            selectItem(mFaculty.get(pos), view);

            return true;
        }
    }

}
