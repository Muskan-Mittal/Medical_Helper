package com.example.muskan.medical_help.Helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.R;

import java.util.List;

/**
 * Created by muskan on 18/12/17.
 */

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    List<medicine_model> medicineList;
    private Activity activity;
    private String[] filepath;
    private String[] filename;
    private static LayoutInflater inflater = null;
    int pos = 0;

    public MedicineAdapter(Activity a, List<medicine_model> medicineList, String[] fpath, String[] fname) {
        this.medicineList = medicineList;
        activity = a;
        filepath = fpath;
        filename = fname;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        MedicineViewHolder viewHolder = new MedicineViewHolder(itemView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(MedicineViewHolder holder, int position) {

        final medicine_model medicine = medicineList.get(position);
        holder.name.setText(medicine.medicineName);
        Bitmap bmp = getResizedBitmap(BitmapFactory.decodeFile(filepath[position]),120);
        holder.ivPoster.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView ivPoster;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cv_name);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
            pos = getAdapterPosition();
        }

    }

    public int getPosition(){
        return pos;
    }

    public void add(medicine_model medicine){ this.medicineList.add(medicine);}

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}