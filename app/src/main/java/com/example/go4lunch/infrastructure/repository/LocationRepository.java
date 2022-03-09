package com.example.go4lunch.infrastructure.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationRepository extends LiveData<Location> {

    private static LocationRepository mLocationFactory = null;
    private final Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LocationRequest mLocationRequest;
    private Location currentLocation;
    private final LocationCallback locationUpdateCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            setCurrentLocation(locationResult.getLastLocation());
            setValue(locationResult.getLastLocation());
        }
    };

    public LocationRepository(Context context) {

        this.context = context.getApplicationContext();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationRequest = LocationRequest.create()
                .setInterval(60000)
                .setFastestInterval(60000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);
    }

    public static LocationRepository getInstance(Context context) {
        if (mLocationFactory == null) {
            synchronized (LocationRepository.class) {
                if (mLocationFactory == null) {
                    mLocationFactory = new LocationRepository(context.getApplicationContext());
                }
            }
        }
        return mLocationFactory;
    }

    public void startService() {
        onActive();
    }

    @Override
    protected void onActive() {
        super.onActive();
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        ) {
            findLocation();
        }
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        fusedLocationProviderClient.removeLocationUpdates(locationUpdateCallback);
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_FINE_LOCATION"}
    )

    private void findLocation() {
        Looper looper = Looper.getMainLooper();
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationUpdateCallback, looper);
    }

    public void stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationUpdateCallback);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
