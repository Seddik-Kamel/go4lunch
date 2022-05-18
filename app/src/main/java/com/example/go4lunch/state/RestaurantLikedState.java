package com.example.go4lunch.state;

import com.example.go4lunch.infrastructure.entity.PlaceEntity;

import java.util.ArrayList;

public class RestaurantLikedState implements MainPageState {

    private final ArrayList<PlaceEntity> restaurantModelArrayList;

    public RestaurantLikedState(ArrayList<PlaceEntity> restaurantModelArrayList) {
        this.restaurantModelArrayList = restaurantModelArrayList;
    }

    public ArrayList<PlaceEntity> getRestaurantModelArrayList() {
        return restaurantModelArrayList;
    }
}
