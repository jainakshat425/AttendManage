package com.example.android.attendmanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.attendmanage.BranchEditActivity;
import com.example.android.attendmanage.R;
import com.example.android.attendmanage.pojos.Branch;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 06-Jan-19.
 */
public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private Context mContext;
    private ArrayList<Branch> mBranches;

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
        int branchId = branch.getBranchId();

        holder.bNameTv.setText(bName);
        holder.bFullNameTv.setText(bFullName);
        if (hodId != null)
            holder.hodIdTv.setText(String.format("HOD: %s", hodId));

        holder.menuButton.setTag(position);
        holder.menuButton.setOnClickListener(view -> {

            PopupMenu popup = new PopupMenu(mContext, holder.menuButton);
            popup.inflate(R.menu.opr_popup_menu);
            popup.setOnMenuItemClickListener(item -> {

                int pos = (int) view.getTag();
                Branch selectedBranch = mBranches.get(pos);

                switch (item.getItemId()) {
                    case R.id.opr_update:
                        Intent intent = new Intent(mContext, BranchEditActivity.class);
                        intent.putExtra(ExtraUtils.EXTRA_BRANCH_OBJ, selectedBranch);
                        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                        mContext.startActivity(intent);
                        return true;
                    case R.id.opr_delete:

                        int bId = selectedBranch.getBranchId();
                        VolleyTask.deleteBranch(mContext, bId, jObj -> {
                            this.mBranches.remove(pos);
                            this.notifyDataSetChanged();
                        });
                        return true;
                    default:
                        return false;
                }
            });
            popup.show();
        });
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

    public class BranchViewHolder extends RecyclerView.ViewHolder {

        private TextView bNameTv;
        private TextView bFullNameTv;
        private TextView hodIdTv;
        private ImageView menuButton;

        public BranchViewHolder(View view) {
            super(view);
            bNameTv = view.findViewById(R.id.branch_name);
            bFullNameTv = view.findViewById(R.id.branch_full_name);
            hodIdTv = view.findViewById(R.id.branch_hod);
            menuButton = view.findViewById(R.id.branch_menu);

        }

    }
}

