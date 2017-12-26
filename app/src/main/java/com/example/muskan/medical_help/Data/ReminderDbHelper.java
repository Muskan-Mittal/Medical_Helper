package com.example.muskan.medical_help.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.muskan.medical_help.Models.reminder_model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muskan on 25/12/17.
 */

public class ReminderDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ReminderDb";
    private static final String TABLE_REMINDERS = "Reminders";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_REPEAT_TYPE = "repeat_type";
    private static final String KEY_ACTIVE = "active";

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DATE + " TEXT,"
                + KEY_TIME + " INTEGER,"
                + KEY_REPEAT_TYPE + " TEXT,"
                + KEY_ACTIVE + " BOOLEAN" + ")";
        db.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);

        // Create tables again
        onCreate(db);
    }

    // Adding new Reminder
    public int addReminder(reminder_model reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DATE, reminder.getSetDate());
        values.put(KEY_TIME, reminder.getReminderTime());
        values.put(KEY_REPEAT_TYPE, reminder.getReminderRepeatType());
        values.put(KEY_ACTIVE, reminder.getReminderActive());

        // Inserting Row
        long ID = db.insert(TABLE_REMINDERS, null, values);
        db.close();
        return (int) ID;
    }

    // Getting single Reminder
    public reminder_model getReminder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[]
                        {
                                KEY_ID,
                                KEY_DATE,
                                KEY_TIME,
                                KEY_REPEAT_TYPE,
                                KEY_ACTIVE
                        }, KEY_ID + "=?",

                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        reminder_model reminder = new reminder_model(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4));

        return reminder;
    }

    // Getting all Reminders
    public List<reminder_model> getAllReminders() {
        List<reminder_model> reminderList = new ArrayList<>();

        // Select all Query
        String selectQuery = "SELECT * FROM " + TABLE_REMINDERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                reminder_model reminder = new reminder_model();
                reminder.setReminderID(Integer.parseInt(cursor.getString(0)));
                reminder.setSetDate(cursor.getString(1));
                reminder.setReminderTime(cursor.getString(2));
                reminder.setReminderRepeatType(cursor.getString(3));
                reminder.setReminderActive(cursor.getString(4));

                // Adding Reminders to list
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        return reminderList;
    }

    // Getting Reminders Count
    public int getRemindersCount() {
        String countQuery = "SELECT * FROM " + TABLE_REMINDERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    // Updating single Reminder
    public int updateReminder(reminder_model reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, reminder.getSetDate());
        values.put(KEY_TIME, reminder.getReminderTime());
        values.put(KEY_REPEAT_TYPE, reminder.getReminderRepeatType());
        values.put(KEY_ACTIVE, reminder.getReminderActive());

        // Updating row
        return db.update(TABLE_REMINDERS, values, KEY_ID + "=?",
                new String[]{String.valueOf(reminder.getReminderID())});
    }

    // Deleting single Reminder
    public void deleteReminder(reminder_model reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, KEY_ID + "=?",
                new String[]{String.valueOf(reminder.getReminderID())});
        db.close();
    }
}

