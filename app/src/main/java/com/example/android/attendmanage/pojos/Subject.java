package com.example.android.attendmanage.pojos;

/**
 * Created by Akshat Jain on 09-Jan-19.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subject implements Parcelable {

    @SerializedName("sub_id")
    @Expose
    private Integer subId;
    @SerializedName("sub_semester")
    @Expose
    private String subSemester;
    @SerializedName("b_name")
    @Expose
    private String bName;
    @SerializedName("sub_name")
    @Expose
    private String subName;
    @SerializedName("sub_full_name")
    @Expose
    private String subFullName;

    public Subject(String subSemester, String bName, String subName, String subFullName) {
        this.subSemester = subSemester;
        this.bName = bName;
        this.subName = subName;
        this.subFullName = subFullName;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public String getSubSemester() {
        return subSemester;
    }

    public void setSubSemester(String subSemester) {
        this.subSemester = subSemester;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubFullName() {
        return subFullName;
    }

    public void setSubFullName(String subFullName) {
        this.subFullName = subFullName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(subId);
        out.writeString(subSemester);
        out.writeString(bName);
        out.writeString(subName);
        out.writeString(subFullName);
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public Subject(Parcel in) {
        subId = in.readInt();
        subSemester = in.readString();
        bName = in.readString();
        subName = in.readString();
        subFullName = in.readString();
    }

}
