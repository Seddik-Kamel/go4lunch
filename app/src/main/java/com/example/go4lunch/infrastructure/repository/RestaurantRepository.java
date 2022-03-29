package com.example.go4lunch.infrastructure.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.dao.RestaurantDao;
import com.example.go4lunch.infrastructure.database.GoLunchDatabase;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;

import java.util.List;

public class RestaurantRepository {

    private final RestaurantDao restaurantDao;
    private final LiveData<List<RestaurantEntity>> allRestaurants;

    public RestaurantRepository(Application application) {
        GoLunchDatabase goLunchDatabase = GoLunchDatabase.getDatabase(application);
        restaurantDao = goLunchDatabase.restaurantDao();
        allRestaurants = restaurantDao.getAlphabetizedRestaurant();
    }


    public LiveData<List<RestaurantEntity>> getAllRestaurants() {
        return allRestaurants;
    }

    public LiveData<RestaurantEntity> getRestaurant(String placeId) {
        return restaurantDao.getRestaurant(placeId);
    }

    public void insert(RestaurantEntity restaurantEntity) {
        GoLunchDatabase.databaseWriteExecutor.execute(() -> restaurantDao.insert(restaurantEntity));
    }

    public void deleteAll() {
        GoLunchDatabase.databaseWriteExecutor.execute(restaurantDao::deleteAll);
    }
}
