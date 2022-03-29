package com.example.go4lunch.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.go4lunch.infrastructure.entity.WorkmateLikedRestaurantEntity;

import java.util.List;

@Dao
public interface WorkmateLikedRestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WorkmateLikedRestaurantEntity workmateLikedRestaurantEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WorkmateLikedRestaurantEntity> workmateLikedRestaurantEntityList);

    @Query("SELECT * FROM workmate_liked_restaurant")
    LiveData<List<WorkmateLikedRestaurantEntity>> getAlphabetizedData();

    @Query("SELECT COUNT(userId) FROM workmate_liked_restaurant WHERE userId =:userId")
    LiveData<Integer> getRowCount(String userId);
}
