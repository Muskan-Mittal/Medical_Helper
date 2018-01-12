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

import com.example.muskan.medical_help.R;

/**
 * Created by muskan on 11/1/18.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordsViewHolder> {

    private Activity activity;
    private String[] filename;
    private String[] filepath;
    private static LayoutInflater inflater = null;
    int pos = 0;

    public RecordsAdapter(Activity a, String[] fpath, String[] fname) {

        activity = a;
        filename = fname;
        filepath = fpath;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_records, null);
        RecordsViewHolder viewHolder = new RecordsViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecordsViewHolder holder, int position) {

        holder.name.setText(filename[position]);
        Bitmap bmp = getResizedBitmap(BitmapFactory.decodeFile(filepath[position]),250);
        holder.iv_record.setImageBitmap(bmp);
    }

    @Override
    public int getItemCount() {
        return filename.length;
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView iv_record;

        public RecordsViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_title);
            iv_record = (ImageView) itemView.findViewById(R.id.img_upload);
            pos = getAdapterPosition();
        }
    }

    public int getPosition() {
        return pos;
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