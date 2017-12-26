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

import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.Helpers.AlarmReceiver;
import com.example.muskan.medical_help.Helpers.InputValidation;
import com.example.muskan.medical_help.Models.medicine_model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by muskan on 19/12/17.
 */

public class UpdateMedicineActivity extends AppCompatActivity {

    int count = 0;

    RadioButton button1, button2, button3, button4, button5;
    CheckBox check1, check2, check3, check4;
    RadioGroup schedule_buttons;
    TextView schedule_text, frontImagePath;
    TextInputLayout textInputLayoutMedicine;
    Button save_btn;
    String routinetime = "";
    Spinner dosage;
    InputValidation input_Validation;

    private medicineDbHelper dbHelper;
    private final AppCompatActivity activity = UpdateMedicineActivity.this;
    private TextInputEditText textInputEditTextMedicine;

    //Alarm variables
    String TAG = "RemindMe";
    LocalData localData;
    NotificationScheduler notificationScheduler;
    String newString = "";
    medicine_model medicineObj;

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

                updateMedicineToSQLite();
                //addAlarms();
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
        check1 = (CheckBox) findViewById(R.id.checkbox_9am);
        check2 = (CheckBox)findViewById(R.id.checkbox_12pm);
        check3 = (CheckBox)findViewById(R.id.checkbox_2pm);
        check4 = (CheckBox)findViewById(R.id.checkbox_9pm);
        input_Validation = new InputValidation();
        notificationScheduler = new NotificationScheduler();
    }

    private void initObjects() {
        medicineObj = new medicine_model();
        dbHelper = new medicineDbHelper(activity);
        localData = new LocalData(getApplicationContext());

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
        /*String str = medicineObj.routineTime;
        String[] routineList = str.split(",");
        for(int j=0; j<routineList.length; j++){
            switch (routineList[j]){
                case "9 AM":
                    check1.setChecked(true);
                    break;
                case "12 PM":
                    check2.setChecked(true);
                    break;
                case "2 PM":
                    check3.setChecked(true);
                    break;
                case "9 PM":
                    check4.setChecked(true);
                    break;
            }
        }*/

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

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        if(checked){
            routinetime += ((CheckBox) view).getText().toString() + ",";
        }


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
        notificationScheduler.setReminder(UpdateMedicineActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());

    }

}
