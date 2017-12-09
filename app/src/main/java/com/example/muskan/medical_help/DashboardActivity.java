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

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        LinearLayout addmedicine_layout = (LinearLayout)findViewById(R.id.addMedicine);
        addmedicine_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent addMedicineIntent = new Intent(DashboardActivity.this, AddMedicineActivity.class);
                startActivity(addMedicineIntent);

            }
        });

       /* titles.add("Emergency");
        titles.add("Settings");
        titles.add("About Us");
        titles.add("Logout");

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        //drawerList = (ListView)findViewById(R.id.);*/

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

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}
