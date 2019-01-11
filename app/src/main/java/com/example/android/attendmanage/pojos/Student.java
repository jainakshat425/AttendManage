package com.example.android.attendmanage.pojos;

/**
 * Created by Akshat Jain on 10-Jan-19.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student implements Parcelable {

    @SerializedName("student_id")
    @Expose
    private Integer studentId;
    @SerializedName("std_roll_no")
    @Expose
    private String stdRollNo;
    @SerializedName("std_name")
    @Expose
    private String stdName;
    @SerializedName("semester")
    @Expose
    private Integer semester;
    @SerializedName("b_name")
    @Expose
    private String bName;
    @SerializedName("section")
    @Expose
    private String section;

    public Student(String stdRollNo, String stdName, Integer semester, String bName, String section) {
        this.stdRollNo = stdRollNo;
        this.stdName = stdName;
        this.semester = semester;
        this.bName = bName;
        this.section = section;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public String getStdRollNo() {
        return stdRollNo;
    }

    public void setStdRollNo(String stdRollNo) {
        this.stdRollNo = stdRollNo;
    }

    public String getStdName() {
        return stdName;
    }

    public void setStdName(String stdName) {
        this.stdName = stdName;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getBName() {
        return bName;
    }

    public void setBName(String bName) {
        this.bName = bName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(studentId);
        out.writeString(stdRollNo);
        out.writeString(stdName);
        out.writeInt(semester);
        out.writeString(bName);
        out.writeString(section);
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student(Parcel in) {
        studentId = in.readInt();
        stdRollNo = in.readString();
        stdName = in.readString();
        semester = in.readInt();
        bName = in.readString();
        section = in.readString();
    }
}
