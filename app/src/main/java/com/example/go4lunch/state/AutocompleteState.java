package com.example.go4lunch.state;

import android.content.Intent;

import com.example.go4lunch.model.PlaceModel;

import java.util.ArrayList;

public class AutocompleteState implements MainPageState {
    private Intent intent;
    private ArrayList<PlaceModel> placeModelArrayList;


    public AutocompleteState(Intent intent){
        this.intent = intent;
    }

    public AutocompleteState(ArrayList<PlaceModel> placeModelArrayList) {
        this.placeModelArrayList = placeModelArrayList;
    }

    public Intent getIntent() {
        return intent;
    }

    public ArrayList<PlaceModel> getRestaurantModelArrayList() {
        return placeModelArrayList;
    }
}
