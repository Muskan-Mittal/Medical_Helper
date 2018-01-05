package com.example.muskan.medical_help;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muskan.medical_help.Data.ReminderDbHelper;
import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.Helpers.AlarmReceiver;
import com.example.muskan.medical_help.Helpers.InputValidation;
import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.Models.reminder_model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by muskan on 19/12/17.
 */

public class UpdateMedicineActivity extends AppCompatActivity {

    int count = 0;
    // Constant Intent String
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";

    RadioButton button1, button2, button3, button4, button5;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    RadioGroup schedule_buttons;
    TextView schedule_text, frontImagePath;
    TextInputLayout textInputLayoutMedicine;
    Button save_btn;
    String routinetime = "";
    Spinner dosage;
    InputValidation input_Validation;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private String mTime;
    private String mDate;
    private long mRepeatTime;
    reminder_model reminder;
    AlarmReceiver alarmReceiver;
    ReminderDbHelper rb;

    private medicineDbHelper dbHelper;
    private final AppCompatActivity activity = UpdateMedicineActivity.this;
    private TextInputEditText textInputEditTextMedicine;

    //Alarm variables
    medicine_model medicineObj;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmedicine);

        initObjects();
        initViews();

        medicineObj = (medicine_model) getIntent().getParcelableExtra("Medicine");
        Log.v("selected", ""+medicineObj.medicineName);

        hideRadioButtons();
        getDataFromSqlite();
//        Handles the edit button
        ImageView pencil = (ImageView) findViewById(R.id.editPencil);
        pencil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (count % 2 == 0)
                    showRadioButtons();
                else
                    hideRadioButtons();
                count++;
            }
        });

//        Handles the textview of the schedule text
        schedule_buttons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checked = (RadioButton) findViewById(schedule_buttons.getCheckedRadioButtonId());
                schedule_text.setText(checked.getText());
            }
        });

//        Handles the save button
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChecked();
                updateMedicineToSQLite();
                decideAlarms();
            }
        });

    }

    private void initViews() {

        textInputLayoutMedicine = (TextInputLayout) findViewById(R.id.textInputLayoutMedicine);
        dosage = (Spinner) findViewById(R.id.dosage);
        textInputEditTextMedicine = (TextInputEditText) findViewById(R.id.medicine_input);
        schedule_text = (TextView) findViewById(R.id.schedule_text);
        frontImagePath = (TextView) findViewById(R.id.frontimagepath);
        schedule_buttons = (RadioGroup) findViewById(R.id.radiogroup);
        save_btn = (Button) findViewById(R.id.save_btn);
        button1 = (RadioButton) findViewById(R.id.btn1);
        button2 = (RadioButton) findViewById(R.id.btn2);
        button3 = (RadioButton) findViewById(R.id.btn3);
        button4 = (RadioButton) findViewById(R.id.btn4);
        button5 = (RadioButton) findViewById(R.id.btn5);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox_9am);
        checkBox2 = (CheckBox)findViewById(R.id.checkbox_12pm);
        checkBox3 = (CheckBox)findViewById(R.id.checkbox_2pm);
        checkBox4 = (CheckBox)findViewById(R.id.checkbox_9pm);
        input_Validation = new InputValidation();
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;
    }

    private void initObjects() {
        medicineObj = new medicine_model();
        dbHelper = new medicineDbHelper(activity);
        rb = new ReminderDbHelper(this);
        alarmReceiver = new AlarmReceiver();
        reminder = new reminder_model();
    }

    public void getDataFromSqlite(){
        textInputEditTextMedicine.setText(medicineObj.medicineName);
        frontImagePath.setText(medicineObj.imagePath);
        schedule_text.setText(medicineObj.schedule);
        dosage.setSelection(medicineObj.dosage);
        int i=0;
        switch (medicineObj.schedule){
            case "Daily until I stop":
                i = 0;
                break;
            case "Weekly":
                i = 1;
                break;
            case "Fortnightly":
                i = 2;
                break;
            case "Monthly":
                i = 3;
                break;
            case "Never":
                i = 4;
                break;
        }

        ((RadioButton) schedule_buttons.getChildAt(i)).setChecked(true);

        String str = medicineObj.routineTime;
        String[] routineList = str.split(",");
        for(int j=0; j<routineList.length; j++){
            switch (routineList[j]){
                case "9 AM":
                    checkBox1.setChecked(true);
                    break;
                case "12 PM":
                    checkBox2.setChecked(true);
                    break;
                case "2 PM":
                    checkBox3.setChecked(true);
                    break;
                case "9 PM":
                    checkBox4.setChecked(true);
                    break;
            }
        }

    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        String strDate = "Added on: " + mdformat.format(calendar.getTime());
        Log.v("Date", strDate);
        return strDate;
    }

    private void updateMedicineToSQLite() {

        if (!input_Validation.isInputEditTextFilled(textInputEditTextMedicine, textInputLayoutMedicine, getString(R.string.error_message_medicine))) {
            return;
        }

        medicineObj.medicineName = (textInputEditTextMedicine.getText().toString().trim());
        medicineObj.dosage = (Integer.parseInt(dosage.getSelectedItem().toString()));
        medicineObj.schedule = (schedule_text.getText().toString());
        medicineObj.routineTime = routinetime;

        dbHelper.updateMedicine(medicineObj);

        Toast.makeText(this, "Medicine updated successfully", Toast.LENGTH_SHORT).show();
        Intent myMedicneIntent = new Intent(UpdateMedicineActivity.this, MyMedicineActivity.class);
        startActivity(myMedicneIntent);

    }

    public void hideRadioButtons() {

        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);
        button5.setVisibility(View.GONE);
    }

    public void showRadioButtons() {

        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);
        button4.setVisibility(View.VISIBLE);
        button5.setVisibility(View.VISIBLE);
    }

    public void getChecked() {

        if(checkBox1.isChecked()){
            routinetime += checkBox1.getText().toString()+",";
        }

        if(checkBox2.isChecked()){
            routinetime += checkBox2.getText().toString()+",";
        }

        if(checkBox3.isChecked()){
            routinetime += checkBox3.getText().toString()+",";
        }

        if(checkBox4.isChecked()){
            routinetime += checkBox4.getText().toString()+",";
        }

        if(routinetime == null){
            routinetime = medicineObj.routineTime;
        }

    }

    public void decideAlarms(){

        String[] routineList = routinetime.split(",");
        for(int j=0; j<routineList.length; j++){
            if(!rb.checkReminder(routineList[j], (schedule_text.getText().toString()))){
                addAlarms(routineList[j]);
            }
            else {
                reminder = rb.getReminder(routineList[j], (schedule_text.getText().toString()));
                updateAlarm(medicineObj.medicineName, routineList[j], reminder.getReminderID());
            }

        }
    }

    public int getHour(String time){
        int hr = 0;
        switch (time){
            case "9 AM":
                hr = 9;
                break;
            case "12 PM":
                hr = 12;
                break;
            case "2 PM":
                hr = 14;
                break;
            case "9 PM":
                hr = 21;
                break;
        }
        Log.v("time", ""+hr);
        return hr;
    }

    public String getTitleForReminder(String time, String repeatType){
        String title="Medicine(s) to be taken now:\n";
        List<medicine_model> medicineList;
        medicineDbHelper helper = new medicineDbHelper(activity);
        medicineList = helper.getAllMedicines();

        for(int i=0; i<medicineList.size(); i++){
            medicine_model medicine = medicineList.get(i);
            if((medicine.routineTime).contains(time)){
                title += "-" + medicine.medicineName + "\n";
            }
        }
        Log.v("title:", ""+title);
        return title;
    }

    public void addAlarms(String time){

        String mActive;

        if(medicineObj.schedule!="Never"){
            mActive = "true";
        }
        else {
            mActive = "false";
        }

        String title = getTitleForReminder(time, medicineObj.schedule);
        // Creating Reminder
        int ID = rb.addReminder(new reminder_model(title, getDateTime(), time, medicineObj.schedule, mActive));

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, 15);
        mCalendar.set(Calendar.MINUTE, 40);
        mCalendar.set(Calendar.SECOND, 0);

        // Check repeat type
        if ((schedule_text.getText().toString()).equals("Daily until I stop")) {
            mRepeatTime = 1 * milDay;
        } else if ((schedule_text.getText().toString()).equals("Weekly")) {
            mRepeatTime = 1 * milWeek;
        } else if ((schedule_text.getText().toString()).equals("Fortnightly")) {
            mRepeatTime = 2 * milWeek;
        } else if ((schedule_text.getText().toString()).equals("Monthly")) {
            mRepeatTime = 1 * milMonth;
        } else {
            return;
        }

        // Create a new notification
        if (mActive.equals("true")) {
            new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, ID, mRepeatTime);
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();

        //onBackPressed();
    }

    public void updateAlarm(String medicineName, String time, int mReceivedID){

        // Set new values in the reminder
        reminder.setTitle(""+getTitleForReminder(time, medicineObj.schedule)+medicineName);
        reminder.setSetDate(mDate);
        reminder.setReminderActive("true");

        // Update reminder
        rb.updateReminder(reminder);

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, getHour(time));
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);

        // Cancel existing notification of the reminder by using its ID
        alarmReceiver.cancelAlarm(getApplicationContext(), mReceivedID);

        // Check repeat type
        if ((schedule_text.getText().toString()).equals("Daily until I stop")) {
            mRepeatTime = 1 * milDay;
        } else if ((schedule_text.getText().toString()).equals("Weekly")) {
            mRepeatTime = 1 * milWeek;
        } else if ((schedule_text.getText().toString()).equals("Fortnightly")) {
            mRepeatTime = 2 * milWeek;
        } else if ((schedule_text.getText().toString()).equals("Monthly")) {
            mRepeatTime = 1 * milMonth;
        } else {
            return;
        }

        // Create a new notification
        if (reminder.getReminderActive().equals("true")) {
            alarmReceiver.setRepeatAlarm(getApplicationContext(), mCalendar, mReceivedID, mRepeatTime);
        }

        // Create toast to confirm update
        Toast.makeText(getApplicationContext(), "Edited",
                Toast.LENGTH_SHORT).show();
        onBackPressed();

    }
}
