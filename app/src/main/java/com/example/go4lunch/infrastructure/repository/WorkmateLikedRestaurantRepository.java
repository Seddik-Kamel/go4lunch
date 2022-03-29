package com.example.go4lunch.infrastructure.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.dao.WorkmateLikedRestaurantDao;
import com.example.go4lunch.infrastructure.database.GoLunchDatabase;
import com.example.go4lunch.infrastructure.entity.WorkmateLikedRestaurantEntity;

import java.util.List;

public class WorkmateLikedRestaurantRepository {

    private final WorkmateLikedRestaurantDao workmateLikedRestaurantDao;
    private final LiveData<List<WorkmateLikedRestaurantEntity>> data;


    public WorkmateLikedRestaurantRepository(Application application) {
        GoLunchDatabase goLunchDatabase = GoLunchDatabase.getDatabase(application);
        this.workmateLikedRestaurantDao = goLunchDatabase.workmateLikedRestaurantDao();
        data = workmateLikedRestaurantDao.getAlphabetizedData();

    }

    public void insertData(WorkmateLikedRestaurantEntity workmateLikedRestaurantEntity) {
        GoLunchDatabase.databaseWriteExecutor.execute(() -> workmateLikedRestaurantDao.insert(workmateLikedRestaurantEntity));
    }

    public void insertAll(List<WorkmateLikedRestaurantEntity> workmateLikedRestaurantEntityList) {
        GoLunchDatabase.databaseWriteExecutor.execute(() -> workmateLikedRestaurantDao.insertAll(workmateLikedRestaurantEntityList));
    }


    public LiveData<Integer> getRowCount(String userId) {
        return workmateLikedRestaurantDao.getRowCount(userId);
    }
}
