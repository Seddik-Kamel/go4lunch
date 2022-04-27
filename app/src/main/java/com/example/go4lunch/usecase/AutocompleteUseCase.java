package com.example.go4lunch.usecase;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.repository.PlaceAutocompleteRepository;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.state.AutocompleteState;

import java.util.ArrayList;

public class AutocompleteUseCase extends MediatorLiveData<AutocompleteState> {

    PlaceAutocompleteRepository placeAutocompleteRepository;
    private ArrayList<RestaurantModel> restaurantModelArrayList;

    public AutocompleteUseCase(PlaceAutocompleteRepository placeAutocompleteRepository){
        this.placeAutocompleteRepository = placeAutocompleteRepository;

        addSource(placeAutocompleteRepository, (source) -> {
            if (!source.equals(restaurantModelArrayList)) {
                restaurantModelArrayList = source;
                notifyObserver();
            }
        });
    }

    public void notifyObserver() {
        if ( restaurantModelArrayList != null)
            setValue(new AutocompleteState(restaurantModelArrayList));
    }

    public void updateRestaurant(RestaurantModel restaurantModel) {
        placeAutocompleteRepository.updateRestaurant(restaurantModel);
    }
}
