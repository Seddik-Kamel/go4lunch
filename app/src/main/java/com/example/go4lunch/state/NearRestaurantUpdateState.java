package com.example.go4lunch.state;

import android.location.Location;

import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.state.MainPageState;

import java.util.ArrayList;

public class NearRestaurantUpdateState implements MainPageState {

    final Location currentLocation;
    final ArrayList<RestaurantModel> restaurantModelArrayList;

    public NearRestaurantUpdateState(Location currentLocation, ArrayList<RestaurantModel> restaurantModelArrayList) {
        this.currentLocation = currentLocation;
        this.restaurantModelArrayList = restaurantModelArrayList;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public ArrayList<RestaurantModel> getRestaurantModelArrayList() {
        return restaurantModelArrayList;
    }
}
