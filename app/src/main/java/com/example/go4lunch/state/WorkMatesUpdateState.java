package com.example.go4lunch.state;

import com.example.go4lunch.model.WorkmateModel;

import java.util.ArrayList;

public class WorkMatesUpdateState {

    private ArrayList<WorkmateModel> workmateModelArrayList;

    public WorkMatesUpdateState(ArrayList<WorkmateModel> workmateModelArrayList) {
        this.workmateModelArrayList = workmateModelArrayList;
    }

    public ArrayList<WorkmateModel> getWorkmateModelArrayList() {
        return workmateModelArrayList;
    }

    public void setWorkmateModelArrayList(ArrayList<WorkmateModel> workmateModelArrayList) {
        this.workmateModelArrayList = workmateModelArrayList;
    }
}
