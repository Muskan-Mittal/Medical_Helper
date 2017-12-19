package com.example.muskan.medical_help;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.Helpers.MedicineAdapter;
import com.example.muskan.medical_help.Models.medicine_model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyMedicineActivity extends AppCompatActivity {
    private final AppCompatActivity activity = MyMedicineActivity.this;
    private RecyclerView medicineRecyclerView;
    private RecyclerView.Adapter medicineAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<medicine_model> medicineList;
    medicineDbHelper dbHelper;
    File file;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymedicine);

        // Locate the image folder in your SD Card
        file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "test");
        // Create a new folder if no folder named SDImageTutorial exist
        file.mkdirs();


        if (file.isDirectory()) {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }


            createMedicineList();
            medicineRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(this);
            medicineAdapter = new MedicineAdapter(this, medicineList, FilePathStrings, FileNameStrings);
            medicineRecyclerView.setLayoutManager(layoutManager);
            medicineRecyclerView.setAdapter(medicineAdapter);
        }
    }

    private void createMedicineList() {
        dbHelper = new medicineDbHelper(activity);
        medicineList = new ArrayList<medicine_model>();
        medicineList = dbHelper.getAllMedicines();
    }
}
