package com.example.android.attendmanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.attendmanage.BranchEditActivity;
import com.example.android.attendmanage.ClassEditActivity;
import com.example.android.attendmanage.R;
import com.example.android.attendmanage.pojos.Branch;
import com.example.android.attendmanage.pojos.Class;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Akshat Jain on 08-Jan-19.
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassViewHolder> {

    private Context mContext;
    private ArrayList<Class> mClasses;

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
        int classId = myClass.getClassId();


        holder.bNameTv.setText(bName);
        holder.semesterTv.setText(ExtraUtils.getSemester(semester));
        if (section != null)
            holder.sectionTv.setText(String.format("Section: %s", section));

        holder.menuButton.setTag(position);
        holder.menuButton.setOnClickListener(view -> {

            PopupMenu popup = new PopupMenu(mContext, holder.menuButton);
            popup.inflate(R.menu.opr_popup_menu);
            popup.setOnMenuItemClickListener(item -> {

                int pos = (int) view.getTag();
                Class selectedClass = mClasses.get(pos);

                switch (item.getItemId()) {
                    case R.id.opr_update:
                        Intent intent = new Intent(mContext, ClassEditActivity.class);
                        intent.putExtra(ExtraUtils.EXTRA_CLASS_OBJ, selectedClass);
                        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                        mContext.startActivity(intent);
                        return true;
                    case R.id.opr_delete:

                        int cId = selectedClass.getClassId();
                        VolleyTask.deleteClass(mContext, cId, jObj -> {
                            this.mClasses.remove(pos);
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
        if (mClasses == null)
            return 0;
        else
            return mClasses.size();
    }

    public void swapList(ArrayList<Class> classes) {
        this.mClasses = classes;
        this.notifyDataSetChanged();
    }

    public class ClassViewHolder extends RecyclerView.ViewHolder {

        private TextView bNameTv;
        private TextView semesterTv;
        private TextView sectionTv;
        private ImageView menuButton;

        public ClassViewHolder(View view) {
            super(view);
            bNameTv = view.findViewById(R.id.class_b_name);
            semesterTv = view.findViewById(R.id.class_semester);
            sectionTv = view.findViewById(R.id.class_section);
            menuButton = view.findViewById(R.id.class_menu);

        }

    }
}

