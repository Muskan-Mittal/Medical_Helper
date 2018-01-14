package com.example.muskan.medical_help;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by muskan on 14/1/18.
 */

public class FullImageActivity extends AppCompatActivity {

    ImageView currentPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        Bundle bundle = getIntent().getExtras();
        String imagePath = bundle.getString("Filepath");
        Log.v("image", "" + imagePath);
        currentPic = findViewById(R.id.currentImage);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        currentPic.setImageBitmap(bitmap);
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
