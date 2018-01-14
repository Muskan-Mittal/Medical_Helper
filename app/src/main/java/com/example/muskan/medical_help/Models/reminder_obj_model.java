package com.example.muskan.medical_help.Models;

import android.support.v7.widget.SwitchCompat;

/**
 * Created by muskan on 14/1/18.
 */

public class reminder_obj_model {
    private SwitchCompat switches;
    private medicine_model medicine;
    public reminder_obj_model(medicine_model medicine, SwitchCompat switchCompat){
        this.medicine = medicine;
        this.switches = switchCompat;
    }

    public void setSwitches(SwitchCompat switches){
        this.switches = switches;
    }

    public SwitchCompat getSwitches(){
        return switches;
    }

    public void setMedicineModel(medicine_model medicine){
        this.medicine = medicine;
    }

    public medicine_model getMedicineModel(){
        return medicine;
    }
}
