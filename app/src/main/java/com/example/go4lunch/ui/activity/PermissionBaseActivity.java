package com.example.go4lunch.ui.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.example.go4lunch.utils.eventBus.LocationEvent;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

public abstract class PermissionBaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected View view;
    protected T binding;
    protected Activity activity;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final boolean locationPermissionGranted = false;



    abstract T getViewBinding();
    abstract Activity getActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initActivity();
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

}
