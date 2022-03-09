package com.example.go4lunch.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

@SuppressWarnings("unused")
public abstract class LocationBaseFragment extends PermissionBaseFragment {


    interface LocationResult {
        void makeSomethingAfterLocationResult();
    }

    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private boolean locationIsChanged;
    private LocationResult locationResultCallback;

    abstract LocationResult initLocationResult();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFusedLocationProvider();
        locationResultCallback = initLocationResult();
    }

    @SuppressWarnings("MissingPermission")
    public void getLocation(LocationResult locationResultCallback) {//UI
        try {
            if (isLocationPermissionGranted()) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        locationResultCallback.makeSomethingAfterLocationResult();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void setFusedLocationProvider() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public void setLocationResultCallback(LocationResult locationResultCallback) {
        this.locationResultCallback = locationResultCallback;
    }

    public LocationResult getLocationResultCallback() {
        return locationResultCallback;
    }

    public boolean isLocationIsChanged() {
        return locationIsChanged;
    }

    public void setLocationIsChanged(boolean locationIsChanged) {
        this.locationIsChanged = locationIsChanged;
    }
}
