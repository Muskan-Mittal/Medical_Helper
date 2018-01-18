package com.example.muskan.medical_help.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by muskan on 25/12/17.
 */

public class reminder_model implements Parcelable {

    public int reminderID;
    public String title;
    public String setDate;
    public String reminderTime;
    public String reminderRepeatType;
    public String reminderActive;

    public reminder_model(){}

    public reminder_model(int reminderID, String title, String setDate, String reminderTime, String reminderRepeatType, String reminderActive) {
        this.reminderID = reminderID;
        this.title = title;
        this.setDate = setDate;
        this.reminderTime = reminderTime;
        this.reminderRepeatType = reminderRepeatType;
        this.reminderActive = reminderActive;
    }

    public reminder_model(String title, String setDate, String reminderTime, String reminderRepeatType, String reminderActive) {
        this.title = title;
        this.setDate = setDate;
        this.reminderTime = reminderTime;
        this.reminderRepeatType = reminderRepeatType;
        this.reminderActive = reminderActive;
    }

    private reminder_model(Parcel in){
        reminderID = in.readInt();
        title = in.readString();
        setDate = in.readString();
        reminderTime = in.readString();
        reminderRepeatType = in.readString();
        reminderActive = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(reminderID);
        parcel.writeString(title);
        parcel.writeString(reminderTime);
        parcel.writeString(setDate);
        parcel.writeString(reminderRepeatType);
        parcel.writeString(reminderActive);
    }

    public static final Parcelable.Creator<reminder_model> CREATOR = new Parcelable.Creator<reminder_model>() {

        public reminder_model createFromParcel(Parcel parcel) {
            return new reminder_model(parcel);
        }

        public reminder_model[] newArray(int i) {
            return new reminder_model[i];
        }
    };
}
