package com.example.go4lunch.usecase;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.infrastructure.repository.FavoriteRestaurantRepository;
import com.example.go4lunch.model.FavoriteRestaurantModel;
import com.example.go4lunch.state.FavoriteRestaurantState;

import java.util.ArrayList;

public class FavoriteRestaurantUseCase extends MediatorLiveData<FavoriteRestaurantState> {

    private ArrayList<FavoriteRestaurantModel> favoriteRestaurantModel;

    private final FavoriteRestaurantRepository favoriteRestaurantRepository;

    public FavoriteRestaurantUseCase(FavoriteRestaurantRepository favoriteRestaurantRepository) {
        this.favoriteRestaurantRepository = favoriteRestaurantRepository;
        addSource(favoriteRestaurantRepository, (source) -> {
            favoriteRestaurantModel = source;
            notifyObserver();
        });
    }

    public void notifyObserver() {
        setValue(new FavoriteRestaurantState(favoriteRestaurantModel));
    }

    public void saveFavoriteRestaurant(RestaurantEntity restaurantEntity) {
        favoriteRestaurantRepository.saveFavoriteRestaurantByWorkmate(restaurantEntity);
    }

    public void deleteFavoriteRestaurant(String placeId) {
        favoriteRestaurantRepository.deleteFavoriteRestaurant(placeId);
    }

    public void updateFavoriteRestaurantListener(String placeId) {
        favoriteRestaurantRepository.updateFavoriteRestaurantListener(placeId);
    }
}
