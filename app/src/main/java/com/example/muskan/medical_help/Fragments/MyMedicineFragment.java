package com.example.muskan.medical_help.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.Helpers.MedicineAdapter;
import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.R;
import com.example.muskan.medical_help.RecyclerItemClickListener;
import com.example.muskan.medical_help.UpdateMedicineActivity;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyMedicineFragment extends Fragment {

    MedicineAdapter medicineAdapter;
    private RecyclerView medicineRecyclerView;
    String newString;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<medicine_model> medicineList;
    medicineDbHelper dbHelper;
    File file;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public MyMedicineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Locate the image folder in your SD Card
        file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "test");
        // Create a new folder if no folder named SDImageTutorial exist
        file.mkdirs();
        View rootview;

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

            if (savedInstanceState == null || !savedInstanceState.containsKey("medicineList")) {
                medicineList = new ArrayList<medicine_model>();
                medicineAdapter = new MedicineAdapter(getActivity(), medicineList, FilePathStrings, FileNameStrings);
                createMedicineList();

            } else {

                newString = (String) savedInstanceState.getSerializable("MedicineName");
                medicineAdapter = new MedicineAdapter(getActivity(), medicineList, FilePathStrings, FileNameStrings);
            }
        }

        rootview = inflater.inflate(R.layout.fragment_mymedicine, container, false);
        medicineRecyclerView = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        medicineAdapter = new MedicineAdapter(getActivity(), medicineList, FilePathStrings, FileNameStrings);
        medicineRecyclerView.setLayoutManager(layoutManager);
        medicineRecyclerView.setAdapter(medicineAdapter);
        medicineRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), medicineRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                ((RecyclerItemClickListener.OnItemClickListener) getActivity()).onItemClick(view, position);
                Intent i = new Intent(getActivity(), UpdateMedicineActivity.class);
                i.putExtra("Medicine", (Parcelable) medicineList.get(position));
                startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                ((RecyclerItemClickListener.OnItemClickListener) getActivity()).onItemLongClick(view, position);
            }
        }));

        return rootview;
    }

    private void createMedicineList() {
        dbHelper = new medicineDbHelper(getActivity());
        medicineList = new ArrayList<medicine_model>();
        medicineList = (ArrayList<medicine_model>) dbHelper.getAllMedicines();
    }
}
