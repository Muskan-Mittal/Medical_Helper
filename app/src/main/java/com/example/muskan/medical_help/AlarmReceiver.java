package com.example.muskan.medical_help;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    String TAG = "AlarmReceiver";
    NotificationScheduler notificationScheduler;


    @Override
    public void onReceive(Context context, Intent intent) {

        notificationScheduler = new NotificationScheduler();

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                LocalData localData = new LocalData(context);
                notificationScheduler.setReminder(context, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                return;
            }
        }



        Log.d(TAG, "onReceive: ");

        //Trigger the notification
        notificationScheduler.showNotification(context, AddMedicineActivity.class,
                "5 medicines to be taken", "Take after lunch");

    }
}