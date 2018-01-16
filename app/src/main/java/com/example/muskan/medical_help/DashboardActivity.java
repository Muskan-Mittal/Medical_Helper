package com.example.muskan.medical_help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mAuth = FirebaseAuth.getInstance();

        LinearLayout addmedicine_layout = (LinearLayout)findViewById(R.id.addMedicine);
        addmedicine_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addMedicineIntent = new Intent(DashboardActivity.this, AddMedicineActivity.class);
                startActivity(addMedicineIntent);

            }
        });

        LinearLayout mymedicine_layout = (LinearLayout)findViewById(R.id.myMedicine);
        mymedicine_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myMedicineIntent = new Intent(DashboardActivity.this, MyMedicineActivity.class);
                startActivity(myMedicineIntent);

            }
        });

        LinearLayout reminders_layout = (LinearLayout)findViewById(R.id.reminder);
        reminders_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent reminderIntent = new Intent(DashboardActivity.this, RemindersMainActivity.class);
                startActivity(reminderIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_logout){
            Intent logoutIntent = new Intent(DashboardActivity.this, LogoutActivity.class);
            startActivity(logoutIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){
            Intent startIntent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();
        }
    }
}
