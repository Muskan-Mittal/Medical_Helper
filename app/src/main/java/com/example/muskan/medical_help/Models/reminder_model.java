package com.example.muskan.medical_help.Models;

/**
 * Created by muskan on 25/12/17.
 */

public class reminder_model {

    private int reminderID;
    private String title;
    private String setDate;
    private String reminderTime;
    private String reminderRepeatType;
    private String reminderActive;


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

    public reminder_model() {
    }

    public int getReminderID() {
        return reminderID;
    }

    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }

    public String getReminderTime() {
        return this.reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getReminderRepeatType() {
        return reminderRepeatType;
    }

    public void setReminderRepeatType(String reminderRepeatType) {
        this.reminderRepeatType = reminderRepeatType;
    }

    public String getReminderActive() {
        return reminderActive;
    }

    public void setReminderActive(String reminderActive) {
        this.reminderActive = reminderActive;
    }
}
