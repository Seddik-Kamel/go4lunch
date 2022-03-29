package com.example.go4lunch.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.infrastructure.repository.WorkmateRepository;
import com.example.go4lunch.usecase.NearRestaurantUseCase;
import com.example.go4lunch.usecase.WorkMatesUseCase;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static LocationRepository locationRepository;
    private static PlaceRepository placeRepository;
    private static WorkmateRepository workmateRepository;
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
        if (modelClass.isAssignableFrom(WorkMateViewModel.class)) {
            return (T) new WorkMateViewModel(new WorkMatesUseCase(workmateRepository));
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

    public static ViewModelFactory getInstance(Context context, Application application) {
        if (viewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (viewModelFactory == null) {
                    if (context != null) {
                        locationRepository = LocationRepository.getInstance(context);
                        viewModelFactory = new ViewModelFactory(locationRepository);
                    }

                    if (context != null && application != null)
                        placeRepository = PlaceRepository.getInstance(context, application, locationRepository);
                    workmateRepository = WorkmateRepository.getInstance();
                }
            }
        }
        return viewModelFactory;
    }

    public <T extends ViewModel> T obtainViewModel(Class<T> modelClass) {
        return viewModelFactory.create(modelClass);
    }
}
