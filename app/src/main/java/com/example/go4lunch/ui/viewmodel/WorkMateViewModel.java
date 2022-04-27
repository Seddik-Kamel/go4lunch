package com.example.go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.WorkmateModel;
import com.example.go4lunch.state.WorkMatesUpdateState;
import com.example.go4lunch.usecase.WorkMatesUseCase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class WorkMateViewModel extends ViewModel {

    private ArrayList<WorkmateModel> workmateList = new ArrayList<>();
    private final WorkMatesUseCase workMatesUseCase;
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

    public void setWorkmateList(ArrayList<WorkmateModel> workmateList) {
        this.workmateList = workmateList;
    }

    public void listenWorkmate() {
        workMatesUseCase.listenWorkmate();
    }

    public void deleteCollection() {
        workMatesUseCase.deleteCollection();
    }
}
