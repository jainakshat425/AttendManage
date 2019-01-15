package com.example.android.attendmanage.pojos;

/**
 * Created by Akshat Jain on 14-Jan-19.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Faculty implements Parcelable {

    @SerializedName("fac_id")
    @Expose
    private Integer facId;
    @SerializedName("fac_user_id")
    @Expose
    private String facUserId;
    @SerializedName("fac_name")
    @Expose
    private String facName;
    @SerializedName("mob_no")
    @Expose
    private String mobNo;
    @SerializedName("dept_name")
    @Expose
    private String deptName;

    public Faculty(Integer facId, String facUserId, String facName, String mobNo, String deptName) {
        this.facId = facId;
        this.facUserId = facUserId;
        this.facName = facName;
        this.mobNo = mobNo;
        this.deptName = deptName;
    }

    public Integer getFacId() {
        return facId;
    }

    public void setFacId(Integer facId) {
        this.facId = facId;
    }

    public String getFacUserId() {
        return facUserId;
    }

    public void setFacUserId(String facUserId) {
        this.facUserId = facUserId;
    }

    public String getFacName() {
        return facName;
    }

    public void setFacName(String facName) {
        this.facName = facName;
    }

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(facId);
        out.writeString(facUserId);
        out.writeString(facName);
        out.writeString(mobNo);
        out.writeString(deptName);
    }

    public static final Parcelable.Creator<Faculty> CREATOR = new Parcelable.Creator<Faculty>() {
        public Faculty createFromParcel(Parcel in) {
            return new Faculty(in);
        }

        public Faculty[] newArray(int size) {
            return new Faculty[size];
        }
    };

    public Faculty(Parcel in) {
        facId = in.readInt();
        facUserId = in.readString();
        facName = in.readString();
        mobNo = in.readString();
        deptName = in.readString();
    }
}
