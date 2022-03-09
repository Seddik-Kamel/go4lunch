package com.example.go4lunch.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.go4lunch.infrastructure.entity.RestaurantEntity;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RestaurantEntity restaurantEntity);

    @Query("DELETE FROM restaurant_table")
    void deleteAll();

    @Query("SELECT * FROM restaurant_table ORDER BY placeId ASC")
    LiveData<List<RestaurantEntity>> getAlphabetizedRestaurant();

    @Query("SELECT * FROM restaurant_table WHERE placeId = :placeId")
    LiveData<RestaurantEntity> getRestaurant(String placeId);
}
