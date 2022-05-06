package com.example.go4lunch.infrastructure.repository;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.dao.LocationDao;
import com.example.go4lunch.infrastructure.database.GoLunchDatabase;
import com.example.go4lunch.infrastructure.entity.LocationEntity;
import com.example.go4lunch.state.LocationState;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


public class LocationRepository extends LiveData<LocationState> {

    private static LocationRepository mLocationFactory = null;
    private final Context context;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LocationRequest mLocationRequest;
    private Location currentLocation;
    private final LocationDao locationDao;
    private LocationEntity lastLocationEntity;

    private final LocationCallback locationUpdateCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location currentLocation = locationResult.getLastLocation();
            GoLunchDatabase.databaseWriteExecutor.execute(() -> {
                boolean isNewLocation;
                lastLocationEntity = locationDao.getLastLocation();

                if (lastLocationEntity != null) {
                    isNewLocation = lastLocationEntity.isNewLocation(currentLocation);
                    if (isNewLocation) {
                        //deleteLocation();
                        insertLocation(currentLocation);
                    }

                } else {
                    insertLocation(currentLocation);
                    isNewLocation = true;
                }

                LocationState locationState = new LocationState(lastLocationEntity, currentLocation, isNewLocation);
                postValue(locationState);
            });

        }
    };

    public LocationRepository(Context context, Application application) {
        GoLunchDatabase goLunchDatabase = GoLunchDatabase.getDatabase(application);
        locationDao = goLunchDatabase.locationDao();
        this.context = context.getApplicationContext();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mLocationRequest = LocationRequest.create()
                .setInterval(60000)
                .setFastestInterval(60000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(20000);
    }

    public static LocationRepository getInstance(Context context, Application application) {
        if (mLocationFactory == null) {
            synchronized (LocationRepository.class) {
                if (mLocationFactory == null) {
                    mLocationFactory = new LocationRepository(context.getApplicationContext(), application);
                }
            }
        }
        return mLocationFactory;
    }

    public void startService() {
        onActive();
    }

    @SuppressLint("MissingPermission")
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

    public void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationUpdateCallback);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void insertLocation(Location location) {
        LocationEntity locationEntity = new LocationEntity(location.getLongitude(), location.getLatitude());
        GoLunchDatabase.databaseWriteExecutor.execute(() -> locationDao.insert(locationEntity));
    }

    public LiveData<LocationEntity> getLastLocationLiveData() {
        return locationDao.getLastLocationLiveData();
    }

    public void deleteLocation() {
        GoLunchDatabase.databaseWriteExecutor.execute(locationDao::deleteAll);
    }

    public LocationEntity getLastLocationEntity() {
        return lastLocationEntity;
    }
}
