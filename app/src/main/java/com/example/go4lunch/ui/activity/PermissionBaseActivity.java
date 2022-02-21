package com.example.go4lunch.ui.activity;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewbinding.ViewBinding;

import com.example.go4lunch.utils.eventBus.LocationEvent;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

public abstract class PermissionBaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected View view;
    protected T binding;
    protected Activity activity;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted = false;



    abstract T getViewBinding();
    abstract Activity getActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initActivity();
        //setPermissionGranted();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.

                EventBus.getDefault().post(new LocationEvent(true));

                Snackbar.make(view, "Permission accordée",
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

    public void getLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

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

    private void setPermissionGranted() { // Permission
        if (getActivity() != null)
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationPermissionGranted = true;
            else
                ((HomeScreenActivity) getActivity()).getLocationPermission();
    }

    private void initBinding() {
        binding = getViewBinding();
        view = binding.getRoot();
        setContentView(view);
    }

    private void initActivity() {
        activity = getActivity();
    }

    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(boolean locationPermissionGranted) {
        this.locationPermissionGranted = locationPermissionGranted;
    }
}
