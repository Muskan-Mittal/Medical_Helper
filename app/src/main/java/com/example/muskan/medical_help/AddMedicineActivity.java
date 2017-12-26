package com.example.muskan.medical_help;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.Helpers.AlarmReceiver;
import com.example.muskan.medical_help.Helpers.InputValidation;
import com.example.muskan.medical_help.Models.medicine_model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddMedicineActivity extends AppCompatActivity {

    int count = 0;

    RadioButton button1, button2, button3, button4, button5;
    RadioGroup schedule_buttons;
    TextView schedule_text, frontimagePath;
    TextInputLayout textInputLayoutMedicine;
    Button camera_btn, save_btn;
    String CurrentPhotoPath;
    Bitmap photo;
    String routinetime = "";
    Spinner dosage;
    InputValidation input_Validation;
    Uri imageUri;

    private medicine_model medicine;
    private medicineDbHelper dbHelper;
    private final AppCompatActivity activity = AddMedicineActivity.this;
    private TextInputEditText textInputEditTextMedicine;

    // Request code for camera
    private final int CAMERA_REQUEST_CODE = 100;
    // Request code for runtime permissions
    private final int REQUEST_CODE_STORAGE_PERMS = 321;

    //Alarm variables
    String TAG = "RemindMe";
    SwitchCompat reminderSwitch;
    LocalData localData;
    TextView tvTime;
    LinearLayout ll_set_time;
    int hour, min;
    ClipboardManager myClipboard;
    NotificationScheduler notificationScheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmedicine);

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

                addMedicineToSQLite();
                //addAlarms();
            }
        });

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
        input_Validation = new InputValidation();
        notificationScheduler = new NotificationScheduler();
    }

    private void initObjects() {
        medicine = new medicine_model();
        dbHelper = new medicineDbHelper(activity);
        localData = new LocalData(getApplicationContext());

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
        if (!input_Validation.isInputEditTextFilled(textInputEditTextMedicine, textInputLayoutMedicine, getString(R.string.error_message_medicine))) {
            return;
        }

            if (!dbHelper.checkMedicine(textInputEditTextMedicine.getText().toString().trim())) {
                medicine.medicineName = (textInputEditTextMedicine.getText().toString().trim());
                medicine.imagePath = (getImageUri().getPath());
                medicine.dosage = (Integer.parseInt(dosage.getSelectedItem().toString()));
                medicine.schedule = (schedule_text.getText().toString());
                medicine.routineTime = routinetime;
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
        String imageFileName = "JPEG_" + timeStamp + "_";
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

    public Uri getImageUri(){
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

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        routinetime += ((CheckBox) view).getText().toString()+",";

       /* switch (view.getId()) {
            case R.id.checkbox_9am:
                if (checked) {
                    localData.set_hour(9);
                    localData.set_min(0);
                    notificationScheduler.setReminder(AddMedicineActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                }
                break;
            case R.id.checkbox_12pm:
                if(checked){
                    localData.set_hour(12);
                    localData.set_min(0);
                    notificationScheduler.setReminder(AddMedicineActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                }
                break;
            case R.id.checkbox_2pm:
                if(checked){
                    localData.set_hour(14);
                    localData.set_min(0);
                    notificationScheduler.setReminder(AddMedicineActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                }
                break;
            case R.id.checkbox_9pm:
                if(checked){
                    localData.set_hour(21);
                    localData.set_min(0);
                    notificationScheduler.setReminder(AddMedicineActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());
                }
                break;*/
    }



    public void addAlarms(){
        localData.set_hour(17);
        localData.set_min(06);
        notificationScheduler.setReminder(AddMedicineActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());

    }

}
