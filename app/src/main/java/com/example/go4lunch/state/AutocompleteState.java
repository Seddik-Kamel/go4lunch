package com.example.go4lunch.state;

import android.content.Intent;

import com.example.go4lunch.model.RestaurantModel;

import java.util.ArrayList;

public class AutocompleteState implements MainPageState {
    private Intent intent;
    private ArrayList<RestaurantModel> restaurantModelArrayList;


    public AutocompleteState(Intent intent){
        this.intent = intent;
    }

    public AutocompleteState(ArrayList<RestaurantModel> restaurantModelArrayList) {
        this.restaurantModelArrayList = restaurantModelArrayList;
    }

    public Intent getIntent() {
        return intent;
    }

    public ArrayList<RestaurantModel> getRestaurantModelArrayList() {
        return restaurantModelArrayList;
    }
}
