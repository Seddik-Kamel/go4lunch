package com.example.go4lunch.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.example.go4lunch.R;
import com.google.android.material.snackbar.Snackbar;

public class MainActivityToDelete2 extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;
    private View mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_to_delete2);
        mLayout = findViewById(R.id.main_layout);

        // Register a listener for the 'Show Camera Preview' button.
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launch();
            }
        });

    }

    private void getLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            Snackbar.make(mLayout, "Location access...",
                    Snackbar.LENGTH_INDEFINITE).setAction("ok", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivityToDelete2.this ,new  String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                }
            }).show();
        } else {

            Snackbar.make(mLayout, "else", Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout,"grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED",
                        Snackbar.LENGTH_SHORT)
                        .show();
                //startCamera();
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "else on requestPermission",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    private void launch() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            Snackbar.make(mLayout,
                    "Permission is already available",
                    Snackbar.LENGTH_SHORT).show();

        } else {
            // Permission is missing and must be requested.
            getLocationPermission();
        }
    }
}