package com.example.muskan.medical_help;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.Models.reminder_model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddMedicineActivity extends AppCompatActivity {

    int count = 0;
    static int medicineNum = 1;
    RadioButton button1, button2, button3, button4, button5;
    RadioGroup schedule_buttons;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    TextView schedule_text, frontimagePath;
    TextInputLayout textInputLayoutMedicine;
    Button camera_btn, save_btn;
    String CurrentPhotoPath;
    String routineTime = "";
    Spinner dosage;
    Uri imageUri;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private String mTime;
    private String mDate;
    private long mRepeatTime;

    private medicine_model medicine;
    private medicineDbHelper dbHelper;
    private final AppCompatActivity activity = AddMedicineActivity.this;
    private TextInputEditText textInputEditTextMedicine;
    private ReminderDbHelper rb;
    reminder_model reminder;
    AlarmReceiver alarmReceiver;
    // Request code for camera
    private final int CAMERA_REQUEST_CODE = 100;
    // Request code for runtime permissions
    private final int REQUEST_CODE_STORAGE_PERMS = 321;
    // Constant Intent String
    public static final String EXTRA_REMINDER_ID = "Reminder_ID";

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
        initToolbar();
        initObjects();
        initViews();
        hideRadioButtons();

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

//        Handles the add photo button
        camera_btn = (Button) findViewById(R.id.camera_button);
        camera_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    if (!hasPermissions()) {
                        requestNecessaryPermissions();
                    } else {
                        dispatchTakePictureIntent();
                    }

                } else {
                    Toast.makeText(AddMedicineActivity.this, "Camera not supported", Toast.LENGTH_LONG).show();
                }
            }

        });

//        Handles the save button
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChecked();
                addMedicineToSQLite();
                decideAlarms();
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_addMedicine);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                }
            });
        }
    }

    private void initViews() {

        textInputLayoutMedicine = (TextInputLayout) findViewById(R.id.textInputLayoutMedicine);
        dosage = (Spinner) findViewById(R.id.dosage);
        textInputEditTextMedicine = (TextInputEditText) findViewById(R.id.medicine_input);
        schedule_text = (TextView) findViewById(R.id.schedule_text);
        frontimagePath = (TextView) findViewById(R.id.frontimagepath);
        schedule_buttons = (RadioGroup) findViewById(R.id.radiogroup);
        save_btn = (Button) findViewById(R.id.save_btn);
        button1 = (RadioButton) findViewById(R.id.btn1);
        button2 = (RadioButton) findViewById(R.id.btn2);
        button3 = (RadioButton) findViewById(R.id.btn3);
        button4 = (RadioButton) findViewById(R.id.btn4);
        button5 = (RadioButton) findViewById(R.id.btn5);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox_9am);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox_12pm);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox_2pm);
        checkBox4 = (CheckBox) findViewById(R.id.checkbox_9pm);
        reminder = new reminder_model();
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
        medicine = new medicine_model();
        dbHelper = new medicineDbHelper(activity);
        rb = new ReminderDbHelper(this);
        alarmReceiver = new AlarmReceiver();

    }

    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        String strDate = "Added on: " + mdformat.format(calendar.getTime());
        Log.v("Date", strDate);
        return strDate;
    }

    private void addMedicineToSQLite() {
        Boolean flag = true;
        String medicineName = textInputEditTextMedicine.getText().toString().trim();
        if (TextUtils.isEmpty(medicineName)) {
            textInputEditTextMedicine.setError("Enter a valid medicine name");
            return;
        }

        if (!dbHelper.checkMedicine(textInputEditTextMedicine.getText().toString().trim())) {
            medicine.medicineName = (textInputEditTextMedicine.getText().toString().trim());
            medicine.imagePath = (getImageUri().getPath());
            medicine.dosage = (Integer.parseInt(dosage.getSelectedItem().toString()));
            medicine.schedule = (schedule_text.getText().toString());
            medicine.routineTime = routineTime;
            medicine.date = getDateTime();
            dbHelper.addMedicine(medicine);

        } else {
            flag = false;
            Toast.makeText(this, "This medicine is already added", Toast.LENGTH_SHORT).show();
            return;
        }

        if (flag) {
            Toast.makeText(this, "Medicine added successfully", Toast.LENGTH_SHORT).show();
            Intent dashboardIntent = new Intent(AddMedicineActivity.this, DashboardActivity.class);
            startActivity(dashboardIntent);
        }
    }

    @SuppressLint("WrongConstant")
    private boolean hasPermissions() {
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private void requestNecessaryPermissions() {

        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults) {

        boolean allowed = true;
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMS:
                for (int res : grandResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
        if (allowed) {
            dispatchTakePictureIntent();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    Toast.makeText(AddMedicineActivity.this, "Camera Permissions denied", Toast.LENGTH_SHORT).show();
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(AddMedicineActivity.this, "Storage Permissions denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("lpl", ex.getMessage());
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String folderName = "test";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = medicineNum + "_" + timeStamp;
        File f = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!f.exists()) {
            f.mkdirs();
        }
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        imageUri = Uri.fromFile(image);
        CurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {

                frontimagePath.setText(CurrentPhotoPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Uri getImageUri() {
        return imageUri;
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

        if (checkBox1.isChecked()) {
            routineTime += checkBox1.getText().toString() + ",";
        }

        if (checkBox2.isChecked()) {
            routineTime += checkBox2.getText().toString() + ",";
        }

        if (checkBox3.isChecked()) {
            routineTime += checkBox3.getText().toString() + ",";
        }

        if (checkBox4.isChecked()) {
            routineTime += checkBox4.getText().toString() + ",";
        }


    }

    public void decideAlarms() {

        String[] routineList = routineTime.split(",");
        for (int j = 0; j < routineList.length; j++) {
            if (!rb.checkReminder(routineList[j], (schedule_text.getText().toString()))) {
                addAlarms(routineList[j]);
            } else {
                reminder = rb.getReminder(routineList[j], (schedule_text.getText().toString()));
                updateAlarm(medicine.medicineName, routineList[j], reminder.getReminderID());
            }

        }
    }

    public int getHour(String time) {
        int hr = 0;
        switch (time) {
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
        return hr;
    }

    public String getTitleForReminder(String time, String repeatType) {
        String title = "Medicine(s) to be taken today:\n";
        List<medicine_model> medicineList;
        medicineDbHelper helper = new medicineDbHelper(activity);
        medicineList = helper.getAllMedicines();

        for (int i = 0; i < medicineList.size(); i++) {
            medicine_model medicine = medicineList.get(i);
            if (medicine.schedule == repeatType && (medicine.routineTime).contains(time)) {
                title += i + ". " + medicine.medicineName + "\n";
            }
        }
        return title;
    }

    public void addAlarms(String time) {

        String mActive;

        if (medicine.schedule != "Never") {
            mActive = "true";
        } else {
            mActive = "false";
        }

        String title = getTitleForReminder(time, medicine.schedule);
        // Creating Reminder
        int ID = rb.addReminder(new reminder_model(title, getDateTime(), time, medicine.schedule, mActive));

        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, getHour(time));
        mCalendar.set(Calendar.MINUTE, 0);
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

    public void updateAlarm(String medicineName, String time, int mReceivedID) {

        // Set new values in the reminder
        reminder.setTitle(getTitleForReminder(time, medicine.schedule));
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
