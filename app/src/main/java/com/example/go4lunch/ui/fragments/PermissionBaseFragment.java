package com.example.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class PermissionBaseFragment extends BaseFragment {
    private boolean locationPermissionGranted;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationPermissionGranted = false;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermissionArgument();
    }

    private void getLocationPermissionArgument() {
        Bundle bundle = getArguments();
        if (bundle != null)
            setLocationPermissionGranted(bundle.getBoolean(MapViewFragment.LOCATION_GRANTED));
    }

    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(boolean locationPermissionGranted) {
        this.locationPermissionGranted = locationPermissionGranted;
    }

}
