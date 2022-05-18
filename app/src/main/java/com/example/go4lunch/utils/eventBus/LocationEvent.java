package com.example.go4lunch.utils.eventBus;

public class LocationEvent {

    public final boolean locationPermissionGranted;

    public LocationEvent(boolean locationPermissionGranted) {
        this.locationPermissionGranted = locationPermissionGranted;
    }
}
