package com.example.muskan.medical_help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.muskan.medical_help.Fragments.MyMedicineFragment;

public class MyMedicineActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyMedicineFragment()).commit();
        initToolbar();
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent i = new Intent(MyMedicineActivity.this, UpdateMedicineActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_myMedicine);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MyMedicineActivity.this, DashboardActivity.class));
                }
            });
        }
    }
}