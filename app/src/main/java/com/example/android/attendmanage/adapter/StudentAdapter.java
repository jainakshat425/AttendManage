package com.example.android.attendmanage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.attendmanage.R;
import com.example.android.attendmanage.StudentEditActivity;
import com.example.android.attendmanage.SubjectEditActivity;
import com.example.android.attendmanage.pojos.Student;
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
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context mContext;
    private ArrayList<Student> mStudents;

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
        int stdId = student.getStudentId();

        holder.serialNoTv.setText(String.valueOf(position + 1));
        holder.stdNameTv.setText(name);
        holder.stdRollNoTv.setText(rollNo);

        holder.menuButton.setTag(position);
        holder.menuButton.setOnClickListener(view -> {

            PopupMenu popup = new PopupMenu(mContext, holder.menuButton);
            popup.inflate(R.menu.opr_popup_menu);
            popup.setOnMenuItemClickListener(item -> {

                int pos = (int) view.getTag();
                Student selectedStudent = mStudents.get(pos);

                switch (item.getItemId()) {
                    case R.id.opr_update:
                        Intent intent = new Intent(mContext, StudentEditActivity.class);
                        intent.putExtra(ExtraUtils.EXTRA_STUDENT_OBJ, selectedStudent);
                        intent.putExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_UPDATE);
                        mContext.startActivity(intent);
                        return true;
                    case R.id.opr_delete:

                        int sId = selectedStudent.getStudentId();
                        VolleyTask.deleteStudent(mContext, sId, jObj -> {
                            this.mStudents.remove(pos);
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
        if (mStudents == null)
            return 0;
        else
            return mStudents.size();
    }

    public void swapList(ArrayList<Student> students) {
        this.mStudents = students;
        this.notifyDataSetChanged();
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {

        private TextView serialNoTv;
        private TextView stdNameTv;
        private TextView stdRollNoTv;
        private ImageView menuButton;

        public StudentViewHolder(View view) {
            super(view);
            serialNoTv = view.findViewById(R.id.std_serial_no);
            stdNameTv = view.findViewById(R.id.std_name);
            stdRollNoTv = view.findViewById(R.id.std_roll_no);
            menuButton = view.findViewById(R.id.std_menu);

        }

    }
}

