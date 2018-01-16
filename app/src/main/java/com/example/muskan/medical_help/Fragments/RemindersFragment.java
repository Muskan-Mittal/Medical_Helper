package com.example.muskan.medical_help.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muskan.medical_help.Data.ReminderDbHelper;
import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.Helpers.MedicineAdapter;
import com.example.muskan.medical_help.Helpers.ReminderAdapter;
import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.Models.reminder_model;
import com.example.muskan.medical_help.MyMedicineActivity;
import com.example.muskan.medical_help.R;
import com.example.muskan.medical_help.RecyclerItemClickListener;
import com.example.muskan.medical_help.UpdateMedicineActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RemindersFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

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
        View rootView;
        rootView = inflater.inflate(R.layout.activity_reminders, container, false);
        reminderRecyclerView = (RecyclerView) rootView.findViewById(R.id.reminderRv);
        layoutManager = new LinearLayoutManager(getActivity());
        reminderAdapter = new ReminderAdapter(getActivity(), medicineList);
        reminderRecyclerView.setLayoutManager(layoutManager);
        reminderRecyclerView.setAdapter(reminderAdapter);
        reminderRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), reminderRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, final int position) {
               /* if (( getActivity()) != null) {
                    ((RecyclerItemClickListener.OnItemClickListener) getActivity()).onItemLongClick(view, position);
                }*/
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(getActivity());
                alertDlg.setMessage("Are you sure you remove this medicine?");
                alertDlg.setCancelable(false);

                alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                dbHelper.deleteMedicine(medicineList.get(position));
                                medicineList.remove(position);
                                reminderAdapter.notifyItemRemoved(position);
                                reminderAdapter.notifyItemRangeChanged(position, medicineList.size());
                                medicine_model medicine = new medicine_model();
                                medicine = medicineList.get(position);
                                File fDelete = new File(medicine.imagePath);
                                if (fDelete.exists()) {
                                    if (fDelete.delete()) {
                                        Log.v("file Deleted :", ""+medicine.imagePath);
                                    } else {
                                        Log.v("file not Deleted :", ""+medicine.imagePath);
                                    }
                                }
                            }
                        }
                );

                alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
                });
                alertDlg.create().show();
            }
        }));

        return rootView;
    }

    private void createMedicineList() {
        dbHelper = new medicineDbHelper(getActivity());
        medicineList = new ArrayList<medicine_model>();
        medicineList = (ArrayList<medicine_model>) dbHelper.getAllMedicines();
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

        Toast.makeText(getActivity(), "Selected "+position, Toast.LENGTH_SHORT).show();
    }
}
