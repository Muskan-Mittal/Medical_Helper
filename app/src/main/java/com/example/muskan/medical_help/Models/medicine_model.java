package com.example.muskan.medical_help.Models;

public class medicine_model {

    private int medicineId;
    private String medicineName;
    private String imagePath;
    private int dosage;
    private String schedule;
    private String routineTime;

    public medicine_model(){}

    public medicine_model(int medicineId, String medicineName, String imagePath, int dosage, String schedule, String routineTime){
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.imagePath = imagePath;
        this.schedule = schedule;
        this.routineTime = routineTime;
    }

    public int getMedicineId(){ return medicineId;}

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




}
