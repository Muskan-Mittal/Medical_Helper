package com.example.muskan.medical_help.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.Helpers.MedicineAdapter;
import com.example.muskan.medical_help.Helpers.ReminderAdapter;
import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemindersFragment extends Fragment {

    ReminderAdapter reminderAdapter;
    private RecyclerView reminderRecyclerView;
    String newString;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<medicine_model> medicineList;
    medicineDbHelper dbHelper;


    public RemindersFragment() {
        // Required empty public constructor
    }

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState == null || !savedInstanceState.containsKey("medicineList")) {
            medicineList = new ArrayList<medicine_model>();
            reminderAdapter = new ReminderAdapter(getActivity(), medicineList);
            createMedicineList();

        } else {

            newString = (String) savedInstanceState.getSerializable("MedicineName");
            reminderAdapter = new ReminderAdapter(getActivity(), medicineList);

        }
        View rootview;
        rootview = inflater.inflate(R.layout.activity_reminders, container, false);
        reminderRecyclerView = (RecyclerView) rootview.findViewById(R.id.reminderRv);
        layoutManager = new LinearLayoutManager(getActivity());
        reminderAdapter = new ReminderAdapter(getActivity(), medicineList);
        reminderRecyclerView.setLayoutManager(layoutManager);
        reminderRecyclerView.setAdapter(reminderAdapter);

        return rootview;
    }

    private void createMedicineList() {
        dbHelper = new medicineDbHelper(getActivity());
        medicineList = new ArrayList<medicine_model>();
        medicineList = (ArrayList<medicine_model>) dbHelper.getAllMedicines();
    }

}
