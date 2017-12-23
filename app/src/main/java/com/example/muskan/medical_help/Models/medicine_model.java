package com.example.muskan.medical_help.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

public class medicine_model implements Parcelable {

    public int medicineId;
    public String medicineName;
    public String imagePath;
    public int dosage;
    public String schedule;
    public String routineTime;
    public String date;

    public medicine_model(){}

    public medicine_model(int medicineId, String medicineName, String imagePath, int dosage, String schedule, String routineTime, String date){
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.imagePath = imagePath;
        this.schedule = schedule;
        this.routineTime = routineTime;
        this.date = date;
    }

    private medicine_model(Parcel in){
        medicineId = in.readInt();
        medicineName = in.readString();
        imagePath = in.readString();
        dosage = in.readInt();
        schedule = in.readString();
        routineTime = in.readString();
        date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(medicineId);
        parcel.writeString(medicineName);
        parcel.writeString(imagePath);
        parcel.writeInt(dosage);
        parcel.writeString(schedule);
        parcel.writeString(routineTime);
        parcel.writeString(date);

    }

    public static final Parcelable.Creator<medicine_model> CREATOR = new Parcelable.Creator<medicine_model>() {

        public medicine_model createFromParcel(Parcel parcel) {
            return new medicine_model(parcel);
        }

        public medicine_model[] newArray(int i) {
            return new medicine_model[i];
        }


    };

    /*public int getMedicineId(){ return medicineId;}

    public void setMedicineId(int medicineId){ this.medicineId = medicineId;}

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getDosage(){ return dosage;}

    public void setDosage(int dosage){ this.dosage = dosage;}

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRoutineTime() {
        return routineTime;
    }

    public void setRoutineTime(String routineTime) {
        this.routineTime = routineTime;
    }

    public String getDate(){ return date; }

    public void setDate (String date){
        this.date = date;
    }*/


}
