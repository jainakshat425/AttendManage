package com.example.android.attendmanage.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Akshat Jain on 11-Jan-19.
 */
public class FacSchedule implements Parcelable {

    @SerializedName("semester")
    @Expose
    private Integer sem;
    @SerializedName("lect_id")
    @Expose
    private Integer lectId;
    @SerializedName("b_name")
    @Expose
    private String bName;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("lect_no")
    @Expose
    private Integer lectNo;
    @SerializedName("sub_name")
    @Expose
    private String subName;
    @SerializedName("lect_start_time")
    @Expose
    private String lectStartTime;
    @SerializedName("lect_end_time")
    @Expose
    private String lectEndTime;
    @SerializedName("fac_email")
    @Expose
    private String facEmail;
    @SerializedName("day")
    @Expose
    private String day;

    public FacSchedule(Integer sem, String bName, String section, Integer lectNo, String subName,
                       String lectStartTime, String lectEndTime, String facEmail, String day) {
        this.sem = sem;
        this.bName = bName;
        this.section = section;
        this.lectNo = lectNo;
        this.subName = subName;
        this.lectStartTime = lectStartTime;
        this.lectEndTime = lectEndTime;
        this.facEmail = facEmail;
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public String getfacEmail() {
        return facEmail;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setfacEmail(String facEmail) {
        this.facEmail = facEmail;
    }

    public Integer getSem() {
        return sem;
    }

    public void setSem(Integer sem) {
        this.sem = sem;
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

    public Integer getLectNo() {
        return lectNo;
    }

    public void setLectNo(Integer lectNo) {
        this.lectNo = lectNo;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getLectStartTime() {
        return lectStartTime;
    }

    public void setLectStartTime(String lectStartTime) {
        this.lectStartTime = lectStartTime;
    }

    public String getLectEndTime() {
        return lectEndTime;
    }

    public void setLectEndTime(String lectEndTime) {
        this.lectEndTime = lectEndTime;
    }

    public Integer getLectId() {
        return lectId;
    }

    public void setLectId(Integer lectId) {
        this.lectId = lectId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(sem);
        out.writeInt(lectId);
        out.writeString(bName);
        out.writeString(section);
        out.writeInt(lectNo);
        out.writeString(subName);
        out.writeString(lectStartTime);
        out.writeString(lectEndTime);
        out.writeString(facEmail);
        out.writeString(day);
    }

    public static final Parcelable.Creator<FacSchedule> CREATOR = new Parcelable.Creator<FacSchedule>() {
        public FacSchedule createFromParcel(Parcel in) {
            return new FacSchedule(in);
        }

        public FacSchedule[] newArray(int size) {
            return new FacSchedule[size];
        }
    };

    public FacSchedule(Parcel in) {
        sem = in.readInt();
        lectId = in.readInt();
        bName = in.readString();
        section = in.readString();
        lectNo = in.readInt();
        subName = in.readString();
        lectStartTime = in.readString();
        lectEndTime = in.readString();
        facEmail = in.readString();
        day = in.readString();

    }
}

