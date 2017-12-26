package com.example.muskan.medical_help.Helpers;

/**
 * Created by muskan on 26/12/17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.muskan.medical_help.Data.ReminderDbHelper;
import com.example.muskan.medical_help.Models.reminder_model;

import java.util.Calendar;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeatType;
    private String mActive;
    private String[] mDateSplit;
    private String[] mTimeSplit;
    private int mYear, mMonth, mHour, mMinute, mDay, mReceivedID;
    private long mRepeatTime;

    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            ReminderDbHelper rb = new ReminderDbHelper(context);
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();

            List<reminder_model> reminders = rb.getAllReminders();

            for (reminder_model rm : reminders) {
                mReceivedID = rm.getReminderID();
                mRepeatType = rm.getReminderRepeatType();
                mActive = rm.getReminderActive();
                mDate = rm.getSetDate();
                mTime = rm.getReminderTime();

                mDateSplit = mDate.split("/");
                mTimeSplit = mTime.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(mTimeSplit[0]);
                mMinute = Integer.parseInt(mTimeSplit[1]);

                mCalendar.set(Calendar.MONTH, --mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);

                // Cancel existing notification of the reminder by using its ID
                // mAlarmReceiver.cancelAlarm(context, mReceivedID);

                // Check repeat type
                if (mRepeatType.equals("Daily until I stop")) {
                    mRepeatTime = 1 * milDay;
                } else if (mRepeatType.equals("Weekly")) {
                    mRepeatTime = 1 * milWeek;
                } else if (mRepeatType.equals("Fortnightly")) {
                    mRepeatTime = 2 * milWeek;
                } else if (mRepeatType.equals("Monthly")) {
                    mRepeatTime = 1 * milMonth;
                }

                // Create a new notification
                if (mActive.equals("true")) {
                    mAlarmReceiver.setRepeatAlarm(context, mCalendar, mReceivedID, mRepeatTime);
                }
            }
        }
    }
}