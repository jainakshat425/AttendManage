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

import com.example.android.CollegeApp.editActivities.BranchEditActivity;
import com.example.android.CollegeApp.R;
import com.example.android.CollegeApp.pojos.Branch;
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
public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private Context mContext;
    private ArrayList<Branch> mBranches;

    private boolean multiSelect = false;
    private ArrayList<Branch> selectedItems = new ArrayList<>();

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
            List<Integer> brIdList = new ArrayList<>();
            for (Branch branch : selectedItems) {

                mBranches.remove(branch);
                brIdList.add(branch.getBranchId());

            }
            VolleyTask.deleteBranches(mContext, brIdList, jObj -> notifyDataSetChanged());
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

    public BranchAdapter(Context context, ArrayList<Branch> branches) {

        this.mContext = context;
        this.mBranches = branches;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.branch_item, parent, false);

        return new BranchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {

        Branch branch = mBranches.get(position);

        String bName = branch.getBName();
        String bFullName = branch.getBFullName();
        String hodId = branch.getHodId();

        holder.bNameTv.setText(bName);
        holder.bFullNameTv.setText(bFullName);
        if (hodId != null)
            holder.hodIdTv.setText(String.format("HOD: %s", hodId));

        holder.itemView.setTag(position);
        if (selectedItems.contains(branch)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

    }

    @Override
    public int getItemCount() {
        if (mBranches == null)
            return 0;
        else
            return mBranches.size();
    }

    public void swapList(ArrayList<Branch> branches) {
        this.mBranches = branches;
        this.notifyDataSetChanged();
    }

    public class BranchViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private TextView bNameTv;
        private TextView bFullNameTv;
        private TextView hodIdTv;

        BranchViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            view.setOnClickListener(this);
            bNameTv = view.findViewById(R.id.branch_name);
            bFullNameTv = view.findViewById(R.id.branch_full_name);
            hodIdTv = view.findViewById(R.id.branch_hod);

        }
        void selectItem(Branch item, View view) {
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
            Branch branch = mBranches.get(pos);

            if (currentActionMode != null) {

                selectItem(branch, view);
            } else {

                Intent intent = new Intent(mContext, BranchEditActivity.class);
                intent.putExtra(ExtraUtils.EXTRA_BRANCH_OBJ, branch);
                intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                mContext.startActivity(intent);
            }

        }

        @Override
        public boolean onLongClick(View view) {

            if (currentActionMode != null) return false;

            currentActionMode = ((AppCompatActivity) mContext).startSupportActionMode(modeCallBack);
            int pos = (int) view.getTag();
            selectItem(mBranches.get(pos), view);

            return true;
        }
    }
}

