package com.example.go4lunch.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.infrastructure.repository.RestaurantRepository;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

    private final RestaurantRepository restaurantRepository;
    private final LiveData<List<RestaurantEntity>> allRestaurants;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        restaurantRepository = new RestaurantRepository(application);
        allRestaurants = restaurantRepository.getAllRestaurants();
    }

    public LiveData<List<RestaurantEntity>> getAllRestaurants(){
        return allRestaurants;
    }

    public void insert(RestaurantEntity restaurantEntity){
        restaurantRepository.insert(restaurantEntity);
    }

    public LiveData<RestaurantEntity> getRestaurant(String placeId){
        return restaurantRepository.getRestaurant(placeId);
    }

}
