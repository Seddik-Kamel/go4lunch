package com.example.go4lunch.state;

import java.util.ArrayList;

public class RestaurantLikedState implements MainPageState {

    private final ArrayList<String> restaurantModelArrayList;

    public RestaurantLikedState(ArrayList<String> restaurantModelArrayList) {
        this.restaurantModelArrayList = restaurantModelArrayList;
    }

    public ArrayList<String> getRestaurantModelArrayList() {
        return restaurantModelArrayList;
    }
}
