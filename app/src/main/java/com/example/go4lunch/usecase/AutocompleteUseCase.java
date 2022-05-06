package com.example.go4lunch.usecase;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.repository.PlaceAutocompleteRepository;
import com.example.go4lunch.model.PlaceModel;
import com.example.go4lunch.state.AutocompleteState;

import java.util.ArrayList;

public class AutocompleteUseCase extends MediatorLiveData<AutocompleteState> {

    PlaceAutocompleteRepository placeAutocompleteRepository;
    private ArrayList<PlaceModel> placeModelArrayList;

    public AutocompleteUseCase(PlaceAutocompleteRepository placeAutocompleteRepository){
        this.placeAutocompleteRepository = placeAutocompleteRepository;

        addSource(placeAutocompleteRepository, (source) -> {
            if (!source.equals(placeModelArrayList)) {
                placeModelArrayList = source;
                notifyObserver();
            }
        });
    }

    public void notifyObserver() {
        if ( placeModelArrayList != null)
            setValue(new AutocompleteState(placeModelArrayList));
    }

    public void updateRestaurant(PlaceModel placeModel) {
        placeAutocompleteRepository.updateRestaurant(placeModel);
    }
}
