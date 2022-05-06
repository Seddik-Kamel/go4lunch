package com.example.go4lunch.state;

import android.location.Location;

import com.example.go4lunch.model.PlaceModel;

import java.util.ArrayList;

public class NearRestaurantUpdateState implements MainPageState {

    Location currentLocation;
    final ArrayList<PlaceModel> placeModelArrayList;

    public NearRestaurantUpdateState(Location currentLocation, ArrayList<PlaceModel> placeModelArrayList) {
        this.currentLocation = currentLocation;
        this.placeModelArrayList = placeModelArrayList;
    }

    public NearRestaurantUpdateState( ArrayList<PlaceModel> placeModelArrayList) {
        this.placeModelArrayList = placeModelArrayList;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public ArrayList<PlaceModel> getRestaurantModelArrayList() {
        return placeModelArrayList;
    }
}
