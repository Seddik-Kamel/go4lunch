package com.example.go4lunch.state;

import android.location.Location;

import com.example.go4lunch.infrastructure.entity.LocationEntity;

public class LocationState implements MainPageState{

    private Location currentLocation;
    private LocationEntity locationEntity;
    private boolean isNewLocation;

    public LocationState(){
    }

    public LocationState(LocationEntity locationEntity, Location currentLocation,  boolean isNewLocation) {
        this.locationEntity = locationEntity;
        this.currentLocation = currentLocation;
        this.isNewLocation = isNewLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LocationEntity getLocationEntity() {
        return locationEntity;
    }

    public void setLocationEntity(LocationEntity locationEntity) {
        this.locationEntity = locationEntity;
    }

    public boolean isNewLocation() {
        return isNewLocation;
    }
}
