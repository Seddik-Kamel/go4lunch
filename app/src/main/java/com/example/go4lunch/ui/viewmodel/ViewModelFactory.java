package com.example.go4lunch.ui.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.usecase.NearRestaurantUseCase;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static LocationRepository locationRepository;
    private static PlaceRepository placeRepository;
    private static ViewModelFactory viewModelFactory;

    private ViewModelFactory(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(new NearRestaurantUseCase(locationRepository, placeRepository));
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

    public static ViewModelFactory getInstance(Context context) {
        if (viewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (viewModelFactory == null) {
                    locationRepository = LocationRepository.getInstance(context);
                    placeRepository = PlaceRepository.getInstance(context);
                    viewModelFactory = new ViewModelFactory(locationRepository);
                }
            }
        }
        return viewModelFactory;
    }

    public <T extends ViewModel> T obtainViewModel(Class<T> modelClass) {
        return viewModelFactory.create(modelClass);
    }
}
