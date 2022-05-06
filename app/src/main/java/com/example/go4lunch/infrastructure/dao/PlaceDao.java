package com.example.go4lunch.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.go4lunch.infrastructure.entity.PlaceEntity;

import java.util.List;

@Dao
public interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaceEntity placeEntity);

    @Update
    void update(PlaceEntity placeEntity);

    @Query("DELETE FROM place_table")
    void deleteAll();

    @Query("SELECT * FROM place_table ORDER BY placeId ASC")
    List<PlaceEntity> getAlphabetizedRestaurant();

    @Query("SELECT * FROM place_table WHERE placeId = :placeId")
    LiveData<PlaceEntity> getRestaurant(String placeId);
}
