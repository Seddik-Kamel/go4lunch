package com.example.go4lunch.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewbinding.ViewBinding;

import com.example.go4lunch.utils.eventBus.LocationEvent;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity <T extends ViewBinding> extends AppCompatActivity {

    protected T binding;
    protected View view;
    protected Activity activity;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    abstract T getViewBinding();
    abstract Activity getActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initActivity();
    }

    private void initBinding() {
        binding = getViewBinding();
        view = binding.getRoot();
        setContentView(view);
    }

    private void initActivity(){
        activity = getActivity();
    }

    public void getLocationPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

            //TODO
            Snackbar.make(view, "Texte à modifier",
                    Snackbar.LENGTH_INDEFINITE).setAction("ok", v -> launchRequestPermission()).show();
        } else {
            //TODO
            Snackbar.make(view, "Texte à modifier", Snackbar.LENGTH_SHORT).show();
            launchRequestPermission();
        }
    }

    private void launchRequestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

                EventBus.getDefault().post(new LocationEvent(true));

                Snackbar.make(view,"Permission accordée",
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                // Permission request was denied.

                EventBus.getDefault().post(new LocationEvent(false));
                Snackbar.make(view, "Permission request was denied",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}
