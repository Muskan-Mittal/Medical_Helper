package com.example.muskan.medical_help.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.muskan.medical_help.Models.medicine_model;

import java.util.ArrayList;
import java.util.List;

public class medicineDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AddMedicines.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_MEDICINE = "Medicines";

    // User Table Columns names
    private static final String COLUMN_MEDICINE_ID = "medicine_id";
    private static final String COLUMN_MEDICINE_NAME = "medicine_name";
    private static final String COLUMN_MEDICINE_IMAGEPATH = "medicine_imgpath";
    private static final String COLUMN_MEDICINE_DOSAGE = "medicine_dosage";
    private static final String COLUMN_MEDICINE_SCHEDULE = "medicine_schedule";
    private static final String COLUMN_MEDICINE_ROUTINETIME = "medicine_routinetime";
    private static final String COLUMN_MEDICINE_DATE = "added_on";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_MEDICINE + "(" +
            COLUMN_MEDICINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_MEDICINE_NAME + " TEXT," +
            COLUMN_MEDICINE_IMAGEPATH + " TEXT," +
            COLUMN_MEDICINE_DOSAGE + " INTEGER," +
            COLUMN_MEDICINE_SCHEDULE + " TEXT," +
            COLUMN_MEDICINE_ROUTINETIME + " TEXT," +
            COLUMN_MEDICINE_DATE + " DATE" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_MEDICINE;

    public medicineDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_USER_TABLE);
        onCreate(sqLiteDatabase);
    }

    public boolean checkMedicine(String medicineName) {

        String[] columns = {
                COLUMN_MEDICINE_ID
        };

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COLUMN_MEDICINE_NAME + " = ?";

        String[] selectionArgs = {medicineName};

        Cursor cursor = db.query(TABLE_MEDICINE,
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

    //    Add medicine
    public void addMedicine(medicine_model medicine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_NAME, medicine.medicineName);
        values.put(COLUMN_MEDICINE_IMAGEPATH, medicine.imagePath);
        values.put(COLUMN_MEDICINE_DOSAGE, medicine.dosage);
        values.put(COLUMN_MEDICINE_SCHEDULE, medicine.schedule);
        values.put(COLUMN_MEDICINE_ROUTINETIME, medicine.routineTime);
        values.put(COLUMN_MEDICINE_DATE, medicine.date);
        // Inserting Row
        db.insert(TABLE_MEDICINE, null, values);
        db.close();
    }

    // Getting medicine
    public medicine_model getMedicine(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEDICINE, new String[]{COLUMN_MEDICINE_ID, COLUMN_MEDICINE_NAME, COLUMN_MEDICINE_IMAGEPATH, COLUMN_MEDICINE_DOSAGE, COLUMN_MEDICINE_SCHEDULE, COLUMN_MEDICINE_ROUTINETIME, COLUMN_MEDICINE_DATE}, COLUMN_MEDICINE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        medicine_model medicine = new medicine_model(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
        cursor.close();
        return medicine;
    }

    // Getting all medicines
    public List<medicine_model> getAllMedicines() {
        List<medicine_model> medicineList = new ArrayList<medicine_model>();
        String selectQuery = "SELECT  * FROM " + TABLE_MEDICINE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                medicine_model medicine = new medicine_model();
                medicine.medicineId = (Integer.parseInt(cursor.getString(0)));
                medicine.medicineName = (cursor.getString(1));
                medicine.imagePath = (cursor.getString(2));
                medicine.dosage = (Integer.parseInt(cursor.getString(3)));
                medicine.schedule = (cursor.getString(4));
                medicine.routineTime = (cursor.getString(5));

                medicineList.add(medicine);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return medicineList;
    }

    // Getting medicine count
    public int getMedicineCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MEDICINE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }

    // Updating medicine
    public int updateMedicine(medicine_model medicine) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_NAME, medicine.medicineName);
        values.put(COLUMN_MEDICINE_IMAGEPATH, medicine.imagePath);
        values.put(COLUMN_MEDICINE_DOSAGE, medicine.dosage);
        values.put(COLUMN_MEDICINE_SCHEDULE, medicine.schedule);
        values.put(COLUMN_MEDICINE_ROUTINETIME, medicine.routineTime);
        int id = db.update(TABLE_MEDICINE, values, COLUMN_MEDICINE_ID + " = ?",
                new String[]{String.valueOf(medicine.medicineId)});
        db.close();
        return id;
    }

    // Deleting medicine
    public void deleteMedicine(medicine_model medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINE, COLUMN_MEDICINE_ID + " = ?",
                new String[]{String.valueOf(medicine.medicineId)});
        db.close();
    }
}