package com.example.go4lunch.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.entity.WorkmateLikedRestaurantEntity;
import com.example.go4lunch.infrastructure.repository.WorkmateLikedRestaurantRepository;

import java.util.List;

public class TestViewmodel extends AndroidViewModel {

    private final WorkmateLikedRestaurantRepository workmateLikedRestaurantRepository;

    public TestViewmodel(@NonNull Application application) {
        super(application);
        workmateLikedRestaurantRepository = new WorkmateLikedRestaurantRepository(application);
    }


    public void insert(WorkmateLikedRestaurantEntity workmateLikedRestaurantEntity){
        workmateLikedRestaurantRepository.insertData(workmateLikedRestaurantEntity);
    }

    public void insertAll(List<WorkmateLikedRestaurantEntity> workmateLikedRestaurantEntityList) {
        workmateLikedRestaurantRepository.insertAll(workmateLikedRestaurantEntityList);
    }

    public LiveData<Integer> getRowCount(String userId) {
        return workmateLikedRestaurantRepository.getRowCount(userId);
    }
}
