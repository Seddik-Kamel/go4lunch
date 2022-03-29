package com.example.go4lunch.usecase;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.repository.WorkmateRepository;
import com.example.go4lunch.model.WorkmateModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class WorkMatesUseCase extends MediatorLiveData<WorkMatesUpdateState> {

    private ArrayList<WorkmateModel> workmateModelArrayList;
    private final WorkmateRepository workmateRepository;

    public WorkMatesUseCase(WorkmateRepository workmateRepository) {
        this.workmateRepository = workmateRepository;

        addSource(workmateRepository, source -> {
            workmateModelArrayList = source;
            notifyObserver();
        });
    }

    public void notifyObserver() {
        if (workmateModelArrayList != null)
            setValue(new WorkMatesUpdateState(workmateModelArrayList));
    }

    public void signOut(Context context) {
        workmateRepository.signOut(context).addOnSuccessListener(task -> {
            Log.i("signout", "ok");
        });
    }

    public Task<DocumentSnapshot> persistUser() {
        return workmateRepository.persistUser();
    }


    public void addSnapShotListener(String placeId) {
        workmateRepository.addSnapShotListener(placeId);
    }

    public void addWorkmateSnapShotListener() {
        workmateRepository.addWorkmateSnapShotListener();
    }

    public void persistWorkmateLikedRestaurant(String placeId) {
        workmateRepository.persistWorkmateLikedRestaurant(placeId);
    }

    public void deleteUserWhoLikedRestaurant(String userUID) {
        workmateRepository.deleteWorkmateWhoLikedRestaurant(userUID);
    }

    public FirebaseUser getCurrentUser() {
        return workmateRepository.getCurrentUser();
    }

    public void deleteCollection(/*List<String> placeId*/) {
        workmateRepository.deleteCollection();
    }

    public void getListRestaurant() {
        workmateRepository.getListRestaurant();
    }
}
