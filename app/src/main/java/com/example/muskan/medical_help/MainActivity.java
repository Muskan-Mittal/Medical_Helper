package com.example.muskan.medical_help;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        Button start_Bt = (Button) findViewById(R.id.get_started);
        start_Bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_main);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}
