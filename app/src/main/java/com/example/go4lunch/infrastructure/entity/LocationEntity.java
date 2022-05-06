package com.example.go4lunch.infrastructure.entity;

import android.location.Location;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "location")
public class LocationEntity {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private double longitude;
    private double latitude;

    public LocationEntity() {

    }

    @Ignore
    public LocationEntity(double longitude, double latitude) {
        updateData(longitude, latitude);
    }

    private void updateData(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public boolean isNewLocation(Location currentLocation) {
        Location lastLocation = new Location("");
        lastLocation.setLongitude(longitude);
        lastLocation.setLatitude(latitude);
        float distance = lastLocation.distanceTo(currentLocation);

        return distance > 0.1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
