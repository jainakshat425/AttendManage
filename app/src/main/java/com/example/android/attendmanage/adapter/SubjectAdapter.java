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
import com.example.android.attendmanage.SubjectEditActivity;
import com.example.android.attendmanage.pojos.Branch;
import com.example.android.attendmanage.pojos.Subject;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 06-Jan-19.
 */
public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private Context mContext;
    private ArrayList<Subject> mSubjects;

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
        int subId = subject.getSubId();

        holder.bNameTv.setText(bName);
        holder.subFullNameTv.setText(String.format("%s (%s)", sFullName, sName));

            holder.semesterTv.setText(ExtraUtils.getSemester(semester));

        holder.menuButton.setTag(position);
        holder.menuButton.setOnClickListener(view -> {

            PopupMenu popup = new PopupMenu(mContext, holder.menuButton);
            popup.inflate(R.menu.opr_popup_menu);
            popup.setOnMenuItemClickListener(item -> {

                int pos = (int) view.getTag();
                Subject selectedSubject = mSubjects.get(pos);

                switch (item.getItemId()) {
                    case R.id.opr_update:
                        Intent intent = new Intent(mContext, SubjectEditActivity.class);
                        intent.putExtra(ExtraUtils.EXTRA_SUBJECT_OBJ, selectedSubject);
                        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                        mContext.startActivity(intent);
                        return true;
                    case R.id.opr_delete:

                        int sId = selectedSubject.getSubId();
                        VolleyTask.deleteSubject(mContext, sId, jObj -> {
                            this.mSubjects.remove(pos);
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
        if (mSubjects == null)
            return 0;
        else
            return mSubjects.size();
    }

    public void swapList(ArrayList<Subject> subjects) {
        this.mSubjects = subjects;
        this.notifyDataSetChanged();
    }

    public class SubjectViewHolder extends RecyclerView.ViewHolder {

        private TextView bNameTv;
        private TextView subFullNameTv;
        private TextView semesterTv;
        private ImageView menuButton;

        public SubjectViewHolder(View view) {
            super(view);
            bNameTv = view.findViewById(R.id.sub_b_name);
            subFullNameTv = view.findViewById(R.id.sub_name);
            semesterTv = view.findViewById(R.id.sub_semester);
            menuButton = view.findViewById(R.id.sub_menu);

        }

    }
}

