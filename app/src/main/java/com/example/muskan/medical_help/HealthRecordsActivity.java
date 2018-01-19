package com.example.muskan.medical_help;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.muskan.medical_help.Helpers.RecordsAdapter;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by muskan on 10/1/18.
 */

public class HealthRecordsActivity extends AppCompatActivity implements RecyclerItemClickListener.OnItemClickListener {

    static int reportNum = 1;
    FloatingActionButton addPhoto;
    Uri currentImageUri;
    RecordsAdapter recordsAdapter;
    private RecyclerView recordsRecyclerView;
    private GridLayoutManager layoutManager;
    ProgressDialog progressDialog;
    File file;
    private List<String> FilePathStrings;
    private List<String> FileNameStrings;
    private File[] listFile;
    Context context = this;
    // Request code for camera
    private final int CAMERA_REQUEST_CODE = 100;
    // Request code for runtime permissions
    private final int REQUEST_CODE_STORAGE_PERMS = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_records);
        initToolbar();
        initViews();

        recordsRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewForHealthRecords);
        layoutManager = new GridLayoutManager(this, 2);
        progressDialog = new ProgressDialog(this);
        getImagesAfterUpdate();

        recordsAdapter = new RecordsAdapter(this, FilePathStrings, FileNameStrings);
        recordsRecyclerView.setLayoutManager(layoutManager);
        recordsRecyclerView.setAdapter(recordsAdapter);
        recordsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recordsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(HealthRecordsActivity.this, FullImageActivity.class);
                i.putExtra("Filepath", FilePathStrings.get(position));
                startActivity(i);
            }

            @Override
            public void onItemLongClick(View view, final int position) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Delete record")
                        .setMessage("Are you sure you want to delete this record?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                File fDelete = new File(FilePathStrings.get(position));
                                if (fDelete.exists()) {
                                    if (fDelete.delete()) {
                                        Log.v("file Deleted :", "" + FilePathStrings.get(position));
                                        FilePathStrings.remove(position);
                                        FileNameStrings.remove(position);
                                        recordsAdapter.notifyItemRemoved(position);
                                        recordsAdapter.notifyItemRangeChanged(position, FileNameStrings.size());
                                        recordsAdapter.notifyItemRangeChanged(position, FilePathStrings.size());
                                    } else {
                                        Log.v("file not Deleted :", "" + FilePathStrings.get(position));
                                    }
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }));

        //Handle add photo button
        addPhoto.setOnClickListener(new View.OnClickListener()

        {
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

        addPhoto = (FloatingActionButton) findViewById(R.id.addRecord);
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
        String imageFileName = "Record" + reportNum + "_" + timeStamp;
        reportNum++;
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
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                recordsAdapter.notifyDataSetChanged();
                getImagesAfterUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getImagesAfterUpdate() {

        // Locate the image folder in your SD Card
        file = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Health Records");
        // Create a new folder if no folder named SDImageTutorial exist
        file.mkdirs();

        if (file.isDirectory()) {
            listFile = file.listFiles();
            // Create a String array for FilePathStrings
            FilePathStrings = new ArrayList<>();
            // Create a String array for FileNameStrings
            FileNameStrings = new ArrayList<>();

            for (int i = 0; i < listFile.length; i++) {
                // Get the path of the image file
                FilePathStrings.add(listFile[i].getAbsolutePath());
                // Get the name image file
                FileNameStrings.add(listFile[i].getName());
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent i = new Intent(HealthRecordsActivity.this, FullImageActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemLongClick(View view, int position) {
    }
}
