package com.example.muskan.medical_help.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.R;

import java.util.List;

/**
 * Created by muskan on 24/12/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {

    List<medicine_model> medicineList;
    private Activity activity;
    private static LayoutInflater inflater = null;
    int pos = 0;

    public ReminderAdapter(Activity a, List<medicine_model> medicineList){
        this.medicineList = medicineList;
        activity = a;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_reminder, parent, false);
        ReminderViewHolder viewHolder = new ReminderViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        final medicine_model medicine = medicineList.get(position);
        holder.name.setText(medicine.medicineName);
        if(medicine.schedule!= "Never"){
            holder.switchCompat.setChecked(true);
        }
        else{
            holder.switchCompat.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public void add(medicine_model medicine){ this.medicineList.add(medicine);}

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public SwitchCompat switchCompat;
        public ReminderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_reminder_label);
            switchCompat = (SwitchCompat) itemView.findViewById(R.id.timerSwitch);
            pos = getAdapterPosition();
        }

    }

}
