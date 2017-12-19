package com.example.muskan.medical_help.Helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muskan.medical_help.Data.medicineDbHelper;
import com.example.muskan.medical_help.LoginActivity;
import com.example.muskan.medical_help.Models.medicine_model;
import com.example.muskan.medical_help.R;

import java.util.ArrayList;
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

        medicine_model medicine = medicineList.get(position);
        holder.name.setText(medicine.getMedicineName());
        //holder.date.setText("Added on: "+medicine.getDate());
        Bitmap bmp = getResizedBitmap(BitmapFactory.decodeFile(filepath[position]),120);
        holder.ivPoster.setImageBitmap(bmp);
    }
    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        public TextView name, date;
        public ImageView ivPoster;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cv_name);
            //date = (TextView) itemView.findViewById(R.id.cv_date);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_poster);
        }
    }

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