package com.example.muskan.medical_help;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.muskan.medical_help.Helpers.RecordsAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by muskan on 10/1/18.
 */

public class HealthRecordsActivity extends AppCompatActivity {

    static int reportNum = 1;
    Button addPhoto;
    Uri currentImageUri;
    ImageView currentImageView;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    StorageTask storageTask;
    // Request code for camera
    private final int CAMERA_REQUEST_CODE = 100;
    // Request code for runtime permissions
    private final int REQUEST_CODE_STORAGE_PERMS = 321;
    RecordsAdapter recordsAdapter;
    private RecyclerView recordsRecyclerView;

    private GridLayoutManager layoutManager;
    ProgressDialog progressDialog;
    File file;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_records);
        initToolbar();
        initViews();

        recordsRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewForHealthRecords);
        layoutManager = new GridLayoutManager(this, 2);

        progressDialog = new ProgressDialog(this);

        // Locate the image folder in your SD Card
        file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Health Records");
        // Create a new folder if no folder named SDImageTutorial exist
        file.mkdirs();
        View rootview;

        if (file.isDirectory()) {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new String[listFile.length];
            // Create a String array for FileNameStrings
            FileNameStrings = new String[listFile.length];

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings[i] = listFile[i].getAbsolutePath();
                // Get the name image file
                FileNameStrings[i] = listFile[i].getName();
            }

            recordsAdapter = new RecordsAdapter(this, FilePathStrings, FileNameStrings);
            recordsRecyclerView.setLayoutManager(layoutManager);
            recordsRecyclerView.setAdapter(recordsAdapter);

            //Handle add photo button
            addPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        if (!hasPermissions()) {
                            requestNecessaryPermissions();
                        } else {
                            dispatchTakePictureIntent();
                        }
                    } else {
                        Toast.makeText(HealthRecordsActivity.this, "Camera not supported", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_healthRecord);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                }
            });
        }
    }

    public void initViews() {

        addPhoto = (Button) findViewById(R.id.addRecord);
        currentImageView = (ImageView) findViewById(R.id.currentImage);
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
    }

    @SuppressLint("WrongConstant")
    private boolean hasPermissions() {
        int res = 0;
        String[] permissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }

    private void requestNecessaryPermissions() {

        String[] permissions = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults) {

        boolean allowed = true;
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMS:
                for (int res : grandResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                allowed = false;
                break;
        }
        if (allowed) {
            dispatchTakePictureIntent();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    Toast.makeText(HealthRecordsActivity.this, "Camera Permissions denied", Toast.LENGTH_SHORT).show();
                } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(HealthRecordsActivity.this, "Storage Permissions denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("lpl", ex.getMessage());
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String folderName = "Health Records";
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "" + timeStamp + "_";
        File f = new File(Environment.getExternalStorageDirectory(), folderName);
        if (!f.exists()) {
            f.mkdirs();
        }
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/" + folderName);
        File image = File.createTempFile(
                imageFileName,
                ".jpeg",
                storageDir
        );
        currentImageUri = Uri.fromFile(image);
        Log.v("Pic", "" + currentImageUri.toString());

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                //Picasso.with(this).load(currentImageUri).into(currentImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
