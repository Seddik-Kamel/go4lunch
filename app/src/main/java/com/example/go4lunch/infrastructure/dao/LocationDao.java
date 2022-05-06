package com.example.go4lunch.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.go4lunch.infrastructure.entity.LocationEntity;

@Dao
public interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocationEntity locationEntity);

    @Query("SELECT * FROM location LIMIT 1")
    LocationEntity getLastLocation();

    @Query("SELECT * FROM location LIMIT 1")
    LiveData<LocationEntity> getLastLocationLiveData();


    @Query("DELETE FROM location")
    void deleteAll();
}
