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

import com.example.muskan.medical_help.Data.ReminderDbHelper;
import com.example.muskan.medical_help.Helpers.TimelineAdapter;
import com.example.muskan.medical_help.Models.reminder_model;
import com.example.muskan.medical_help.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {

    List<reminder_model> rmList;
    ReminderDbHelper rmDbHelper;
    TimelineAdapter timelineAdapter;
    private RecyclerView timelineRecyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public TimelineFragment() {
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
        rmList = new ArrayList<reminder_model>();
        timelineAdapter = new TimelineAdapter(getActivity(), rmList);
        createReminderList();

        View rootView;
        rootView = inflater.inflate(R.layout.activity_timeline, container, false);
        timelineRecyclerView = (RecyclerView) rootView.findViewById(R.id.timelineRv);
        layoutManager = new LinearLayoutManager(getActivity());
        timelineAdapter = new TimelineAdapter(getActivity(), rmList);
        timelineRecyclerView.setLayoutManager(layoutManager);
        timelineRecyclerView.setAdapter(timelineAdapter);

        return rootView;
    }

    private void createReminderList() {
        rmDbHelper = new ReminderDbHelper(getActivity());
        rmList = new ArrayList<reminder_model>();
        rmList = (ArrayList<reminder_model>) rmDbHelper.getAllReminders();
    }

}
