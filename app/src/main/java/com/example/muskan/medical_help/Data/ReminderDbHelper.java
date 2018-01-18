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
    private static final String KEY_TITLE = "title";
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
                + KEY_TITLE + " TEXT,"
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

    public boolean checkReminder(String reminderTime, String reminderType) {

        String[] columns = {
                KEY_ID
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = KEY_TIME + " = ? and " + KEY_REPEAT_TYPE + " = ?";

        String[] selectionArgs = {reminderTime, reminderType};

        Cursor cursor = db.query(TABLE_REMINDERS,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    // Adding new Reminder
    public int addReminder(reminder_model reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, reminder.title);
        values.put(KEY_DATE, reminder.setDate);
        values.put(KEY_TIME, reminder.reminderTime);
        values.put(KEY_REPEAT_TYPE, reminder.reminderRepeatType);
        values.put(KEY_ACTIVE, reminder.reminderActive);

        // Inserting Row
        long ID = db.insert(TABLE_REMINDERS, null, values);
        db.close();
        return (int) ID;
    }

    // Getting single Reminder from time
    public reminder_model getReminder(String time, String type) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[]
                        {
                                KEY_ID,
                                KEY_TITLE,
                                KEY_DATE,
                                KEY_TIME,
                                KEY_REPEAT_TYPE,
                                KEY_ACTIVE
                        }, KEY_TIME + "=? and " + KEY_REPEAT_TYPE + "=?",

                new String[]{String.valueOf(time), type}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        reminder_model reminder = new reminder_model(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        cursor.close();
        return reminder;
    }


    // Getting single Reminder
    public reminder_model getReminder(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REMINDERS, new String[]
                        {
                                KEY_ID,
                                KEY_TITLE,
                                KEY_DATE,
                                KEY_TIME,
                                KEY_REPEAT_TYPE,
                                KEY_ACTIVE
                        }, KEY_ID + "=?",

                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        reminder_model reminder = new reminder_model(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        cursor.close();
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
                reminder.reminderID = (Integer.parseInt(cursor.getString(0)));
                reminder.setDate = (cursor.getString(1));
                reminder.reminderTime = (cursor.getString(2));
                reminder.reminderRepeatType = (cursor.getString(3));
                reminder.reminderActive = (cursor.getString(4));

                // Adding Reminders to list
                reminderList.add(reminder);
            } while (cursor.moveToNext());
        }
        cursor.close();
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
        values.put(KEY_TITLE, reminder.title);
        values.put(KEY_DATE, reminder.setDate);
        values.put(KEY_TIME, reminder.reminderTime);
        values.put(KEY_REPEAT_TYPE, reminder.reminderRepeatType);
        values.put(KEY_ACTIVE, reminder.reminderActive);

        // Updating row
        return db.update(TABLE_REMINDERS, values, KEY_ID + "=?",
                new String[]{String.valueOf(reminder.reminderID)});
    }

    // Deleting single Reminder
    public void deleteReminder(reminder_model reminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, KEY_ID + "=?",
                new String[]{String.valueOf(reminder.reminderID)});
        db.close();
    }
}

