package com.example.go4lunch.state;

import com.example.go4lunch.model.FavoriteRestaurantModel;

import java.util.ArrayList;

public class FavoriteRestaurantState {

    private final ArrayList<FavoriteRestaurantModel> favoriteRestaurantModel;

    public FavoriteRestaurantState(ArrayList<FavoriteRestaurantModel> favoriteRestaurantModelArrayList) {
        this.favoriteRestaurantModel = favoriteRestaurantModelArrayList;
    }

    public ArrayList<FavoriteRestaurantModel> getFavoriteRestaurantModel() {
        return favoriteRestaurantModel;
    }
}
