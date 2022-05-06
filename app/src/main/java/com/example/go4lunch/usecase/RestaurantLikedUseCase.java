package com.example.go4lunch.usecase;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.repository.PlaceLikedRepository;
import com.example.go4lunch.state.RestaurantLikedState;

import java.util.ArrayList;

public class RestaurantLikedUseCase extends MediatorLiveData<RestaurantLikedState> {

    private ArrayList<String> restaurantModelArrayList;
    private final PlaceLikedRepository placeLikedRepository;

    public RestaurantLikedUseCase(PlaceLikedRepository placeLikedRepository) {
        this.placeLikedRepository = placeLikedRepository;
        addSource(placeLikedRepository, (source) -> {
            restaurantModelArrayList = source;
            notifyObserver();

        });
    }

    public void notifyObserver() {
        if (restaurantModelArrayList != null)
            setValue(new RestaurantLikedState(restaurantModelArrayList));
    }

    public void saveRestaurantsLiked(String placeId) {
        placeLikedRepository.saveRestaurantsLiked(placeId);
    }

    public void deleteRestaurantLiked(String placeId) {
        placeLikedRepository.deleteRestaurantLikedByWorkmate(placeId);
    }

    public void getRestaurantsLiked(String placeId) {
        placeLikedRepository.getRestaurantsLiked(placeId);
    }
}
