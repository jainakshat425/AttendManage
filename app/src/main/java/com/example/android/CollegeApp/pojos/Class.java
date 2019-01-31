package com.example.android.CollegeApp.pojos;

/**
 * Created by Akshat Jain on 08-Jan-19.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Class implements Parcelable {

    @SerializedName("class_id")
    @Expose
    private Integer classId;
    @SerializedName("semester")
    @Expose
    private Integer semester;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("branch_name")
    @Expose
    private String branchName;


    public Class(int classId, Integer semester, String section, String branchName) {
        this.classId = classId;
        this.semester = semester;
        this.section = section;
        this.branchName = branchName;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(classId);
        out.writeInt(semester);
        out.writeString(section);
        out.writeString(branchName);
    }

    public static final Parcelable.Creator<Class> CREATOR = new Parcelable.Creator<Class>() {
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        public Class[] newArray(int size) {
            return new Class[size];
        }
    };

    public Class(Parcel in) {
        classId = in.readInt();
        semester = in.readInt();
        section = in.readString();
        branchName = in.readString();
    }

}
