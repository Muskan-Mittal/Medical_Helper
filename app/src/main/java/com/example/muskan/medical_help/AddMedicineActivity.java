package com.example.muskan.medical_help;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class AddMedicineActivity extends AppCompatActivity {
    int count=0;
    RadioButton button1, button2, button3, button4, button5;
    RadioGroup schedule_buttons;
    TextView schedule_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmedicine);
        hideRadioButtons();
        Spinner dosage = (Spinner) findViewById(R.id.dosage);

        ImageView pencil = (ImageView)findViewById(R.id.editPencil);
        pencil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(count%2==0)
                    showRadioButtons();
                else
                    hideRadioButtons();
                count++;
            }
        });

        schedule_text = (TextView)findViewById(R.id.schedule_text);
        schedule_buttons = (RadioGroup)findViewById(R.id.radiogroup);

        schedule_buttons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                 RadioButton checked = (RadioButton) findViewById(schedule_buttons.getCheckedRadioButtonId());
                 schedule_text.setText(checked.getText());
            }
        });

    }

    public void hideRadioButtons(){
        button1 = (RadioButton) findViewById(R.id.btn1);
        button1.setVisibility(View.GONE);
        button2 = (RadioButton) findViewById(R.id.btn2);
        button2.setVisibility(View.GONE);
        button3 = (RadioButton) findViewById(R.id.btn3);
        button3.setVisibility(View.GONE);
        button4 = (RadioButton) findViewById(R.id.btn4);
        button4.setVisibility(View.GONE);
        button5 = (RadioButton) findViewById(R.id.btn5);
        button5.setVisibility(View.GONE);
    }

    public void showRadioButtons(){
        button1 = (RadioButton) findViewById(R.id.btn1);
        button1.setVisibility(View.VISIBLE);
        button2 = (RadioButton) findViewById(R.id.btn2);
        button2.setVisibility(View.VISIBLE);
        button3 = (RadioButton) findViewById(R.id.btn3);
        button3.setVisibility(View.VISIBLE);
        button4 = (RadioButton) findViewById(R.id.btn4);
        button4.setVisibility(View.VISIBLE);
        button5 = (RadioButton) findViewById(R.id.btn5);
        button5.setVisibility(View.VISIBLE);
    }



    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.checkbox_9am:
                if (checked) {

                }
                break;

        }
    }
}
