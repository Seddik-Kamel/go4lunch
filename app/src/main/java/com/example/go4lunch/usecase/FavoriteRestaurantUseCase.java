package com.example.go4lunch.usecase;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.entity.PlaceEntity;
import com.example.go4lunch.infrastructure.repository.FavoritePlacesRepository;
import com.example.go4lunch.model.FavoriteRestaurantModel;
import com.example.go4lunch.state.FavoriteRestaurantState;

import java.util.ArrayList;

public class FavoriteRestaurantUseCase extends MediatorLiveData<FavoriteRestaurantState> {

    private ArrayList<FavoriteRestaurantModel> favoriteRestaurantModel;

    private final FavoritePlacesRepository favoritePlacesRepository;

    public FavoriteRestaurantUseCase(FavoritePlacesRepository favoritePlacesRepository) {
        this.favoritePlacesRepository = favoritePlacesRepository;
        addSource(favoritePlacesRepository, (source) -> {
            favoriteRestaurantModel = source;
            notifyObserver();
        });
    }

    public void notifyObserver() {
        setValue(new FavoriteRestaurantState(favoriteRestaurantModel));
    }

    public void saveFavoriteRestaurant(PlaceEntity placeEntity) {
        favoritePlacesRepository.saveFavoriteRestaurantByWorkmate(placeEntity);
    }

    public void deleteFavoriteRestaurant(String placeId) {
        favoritePlacesRepository.deleteFavoriteRestaurant(placeId);
    }

    public void updateFavoriteRestaurantListener(String placeId) {
        favoritePlacesRepository.updateFavoriteRestaurantListener(placeId);
    }
}
