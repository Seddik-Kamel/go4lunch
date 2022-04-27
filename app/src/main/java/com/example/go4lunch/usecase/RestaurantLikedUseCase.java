package com.example.go4lunch.usecase;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.repository.RestaurantLikedRepository;
import com.example.go4lunch.state.RestaurantLikedState;

import java.util.ArrayList;

public class RestaurantLikedUseCase extends MediatorLiveData<RestaurantLikedState> {

    private ArrayList<String> restaurantModelArrayList;
    private final RestaurantLikedRepository restaurantLikedRepository;

    public RestaurantLikedUseCase(RestaurantLikedRepository restaurantLikedRepository) {
        this.restaurantLikedRepository = restaurantLikedRepository;
        addSource(restaurantLikedRepository, (source) -> {
            restaurantModelArrayList = source;
            notifyObserver();

        });
    }

    public void notifyObserver() {
        if (restaurantModelArrayList != null)
            setValue(new RestaurantLikedState(restaurantModelArrayList));
    }

    public void saveRestaurantsLiked(String placeId) {
        restaurantLikedRepository.saveRestaurantsLiked(placeId);
    }

    public void deleteRestaurantLiked(String placeId) {
        restaurantLikedRepository.deleteRestaurantLikedByWorkmate(placeId);
    }

    public void getRestaurantsLiked(String placeId) {
        restaurantLikedRepository.getRestaurantsLiked(placeId);
    }
}
