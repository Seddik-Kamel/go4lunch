package com.example.go4lunch.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.infrastructure.entity.WorkmateLikedRestaurantEntity;
import com.example.go4lunch.model.WorkmateModel;
import com.example.go4lunch.usecase.WorkMatesUpdateState;
import com.example.go4lunch.usecase.WorkMatesUseCase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkMateViewModel extends ViewModel {

    private ArrayList<WorkmateModel> workmateList = new ArrayList<>();
    private WorkMatesUseCase workMatesUseCase;
    private final MutableLiveData<WorkMatesUpdateState> _state = new MutableLiveData<>();
    public final LiveData<WorkMatesUpdateState> state = _state;

    public WorkMateViewModel(WorkMatesUseCase workMatesUseCase) {
        this.workMatesUseCase = workMatesUseCase;

    }

    public void onLoadView() {
        workMatesUseCase.observeForever(workMatesUpdateState -> _state.setValue(new WorkMatesUpdateState(workMatesUpdateState.getWorkmateModelArrayList())));
    }

    public Task<DocumentSnapshot> persistUser() {
        return workMatesUseCase.persistUser();
    }

    public ArrayList<WorkmateModel> getWorkmateList() {
        return workmateList;
    }

    public List<WorkmateLikedRestaurantEntity> getWorkmateLikedRestaurantEntity(ArrayList<WorkmateModel> workmateList, String placeId, Date date) {
        List<WorkmateLikedRestaurantEntity> workmateLikedRestaurantEntityList = new ArrayList<>();
        for (WorkmateModel workmateModel : workmateList) {
            workmateLikedRestaurantEntityList.add(WorkmateLikedRestaurantEntity.updateData(workmateModel, placeId, date));
        }
        return workmateLikedRestaurantEntityList;
    }

    public void setWorkmateList(ArrayList<WorkmateModel> workmateList) {
        this.workmateList = workmateList;
    }

    public void persistWorkmateLikedRestaurant(String placeId) {
        this.workMatesUseCase.persistWorkmateLikedRestaurant(placeId);
    }

    public void addListener(String placeId) {
        workMatesUseCase.addSnapShotListener(placeId);
    }

    public void addWorkmateSnapShotListener(){
        workMatesUseCase.addWorkmateSnapShotListener();
    }

    public void signOut(Context applicationContext) {
        workMatesUseCase.signOut(applicationContext);
    }

    public void deleteUserWhoLikedRestaurant(String userUID) {
        workMatesUseCase.deleteUserWhoLikedRestaurant(userUID);
    }

    public FirebaseUser getCurrentUser() {
        return workMatesUseCase.getCurrentUser();
    }

    public void deleteCollection(/*List<String> placeId*/) {
        workMatesUseCase.deleteCollection();
    }

    public boolean hasLikedThisRestaurant(ArrayList<WorkmateModel> workmateList) {
        String currentUserId = getCurrentUser().getUid();
        boolean isLikedRestaurant = false;
        for (WorkmateModel workmateModel : workmateList) {
            isLikedRestaurant = workmateModel.getUserUid().equals(currentUserId);
        }
        return isLikedRestaurant;
    }

    public void getListRestaurant() {
        workMatesUseCase.getListRestaurant();
    }
}
