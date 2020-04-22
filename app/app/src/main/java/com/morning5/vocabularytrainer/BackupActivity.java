package com.morning5.vocabularytrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.morning5.vocabularytrainer.database.BackupHelper;

public class BackupActivity extends AppCompatActivity {

    public static final int PICKFILE_RESULT_CODE = 1;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_DATA = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_DATA = 3;

    Button button_export;
    Button button_import;

    BackupHelper backupHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

        button_export = findViewById(R.id.button_export);
        button_import = findViewById(R.id.button_import);

        backupHelper = new BackupHelper(getApplicationContext());

        button_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(BackupActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(BackupActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(BackupActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_DATA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    backupHelper.exportData();
                    Toast.makeText(BackupActivity.this, "Exported", Toast.LENGTH_LONG).show();
                }
            }
        });

        button_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //backupHelper.importData();
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(BackupActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(BackupActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(BackupActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_DATA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    String filePath = fileUri.getPath().replace("document/raw:","");
                    backupHelper.importData(filePath);

                    Toast.makeText(this, "Imported", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_DATA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    backupHelper.exportData();
                    Toast.makeText(this, "Exported", Toast.LENGTH_LONG).show();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_DATA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
}
