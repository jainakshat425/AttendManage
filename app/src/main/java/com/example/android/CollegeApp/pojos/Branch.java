package com.example.android.CollegeApp.pojos;

/**
 * Created by Akshat Jain on 06-Jan-19.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Branch implements Parcelable {

    @SerializedName("branch_id")
    @Expose
    private Integer id;
    @SerializedName("b_name")
    @Expose
    private String bName;
    @SerializedName("b_full_name")
    @Expose
    private String bFullName;
    @SerializedName("hod_id")
    @Expose
    private String hodId;

    public Branch(int branchId, String bName, String bFullName, String hodId) {
        this.id = branchId;
        this.bName = bName;
        this.bFullName = bFullName;
        this.hodId = hodId;
    }

    public Integer getBranchId() {
        return id;
    }

    public void setBranchId(Integer id) {
        this.id = id;
    }

    public String getBName() {
        return bName;
    }

    public void setBName(String bName) {
        this.bName = bName;
    }

    public String getBFullName() {
        return bFullName;
    }

    public void setBFullName(String bFullName) {
        this.bFullName = bFullName;
    }

    public void setHodId(String hodId) {
        this.hodId = hodId;
    }

    public String getHodId() {
        return hodId;
    }


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(id);
        out.writeString(bName);
        out.writeString(bFullName);
        out.writeString(hodId);
    }

    public static final Parcelable.Creator<Branch> CREATOR = new Parcelable.Creator<Branch>() {
        public Branch createFromParcel(Parcel in) {
            return new Branch(in);
        }

        public Branch[] newArray(int size) {
            return new Branch[size];
        }
    };

    public Branch(Parcel in) {
        id = in.readInt();
        bName = in.readString();
        bFullName = in.readString();
        hodId = in.readString();
    }
}
