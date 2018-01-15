package com.example.muskan.medical_help.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muskan.medical_help.Models.reminder_model;
import com.example.muskan.medical_help.R;

import java.util.List;

/**
 * Created by muskan on 3/1/18.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {

    List<reminder_model> rmList;
    private Activity activity;
    private static LayoutInflater inflater = null;
    int pos = 0;

    public TimelineAdapter(Activity a, List<reminder_model> reminderList){
        this.rmList = reminderList;
        activity = a;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public TimelineAdapter.TimelineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_timeline, parent, false);
        TimelineAdapter.TimelineViewHolder viewHolder = new TimelineAdapter.TimelineViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TimelineAdapter.TimelineViewHolder holder, int position) {
        final reminder_model reminder = rmList.get(position);
        Log.v("Reminder", ""+reminder.getTitle());
        holder.name.setText("Title:" + reminder.getTitle());
        holder.time.setText("Time:" + reminder.getReminderTime());
    }

    @Override
    public int getItemCount() {
        return rmList.size();
    }

    public void add(reminder_model reminder){ this.rmList.add(reminder); }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView time;
        public TimelineViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_reminder_title);
            time = (TextView) itemView.findViewById(R.id.tv_reminder_time);
            pos = getAdapterPosition();
        }

    }

}
