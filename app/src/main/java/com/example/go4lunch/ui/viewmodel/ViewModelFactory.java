package com.example.go4lunch.ui.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.infrastructure.repository.FavoriteRestaurantRepository;
import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.infrastructure.repository.PlaceAutocompleteRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.infrastructure.repository.RestaurantLikedRepository;
import com.example.go4lunch.infrastructure.repository.WorkmateRepository;
import com.example.go4lunch.usecase.AutocompleteUseCase;
import com.example.go4lunch.usecase.FavoriteRestaurantUseCase;
import com.example.go4lunch.usecase.NearRestaurantUseCase;
import com.example.go4lunch.usecase.RestaurantLikedUseCase;
import com.example.go4lunch.usecase.WorkMatesUseCase;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static LocationRepository locationRepository;
    private static PlaceRepository placeRepository;
    private static PlaceAutocompleteRepository placeAutocompleteRepository;
    private static WorkmateRepository workmateRepository;
    private static RestaurantLikedRepository restaurantLikedRepository;
    private static FavoriteRestaurantRepository favoriteRestaurantRepository;

    private static ViewModelFactory viewModelFactory;
    private final Application application;

    private ViewModelFactory(LocationRepository locationRepository, Application application) {
        ViewModelFactory.locationRepository = locationRepository;
        this.application = application;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(
                    new NearRestaurantUseCase(locationRepository, placeRepository),
                    new RestaurantLikedUseCase(restaurantLikedRepository),
                    new AutocompleteUseCase(placeAutocompleteRepository),application, locationRepository);
        }
        if (modelClass.isAssignableFrom(WorkMateViewModel.class)) {
            return (T) new WorkMateViewModel(new WorkMatesUseCase(workmateRepository));
        }

        if (modelClass.isAssignableFrom(RestaurantDetailViewModel.class))
            return (T) new RestaurantDetailViewModel(application,
                    new FavoriteRestaurantUseCase(favoriteRestaurantRepository),
                    new WorkMatesUseCase(workmateRepository),
                    new RestaurantLikedUseCase(restaurantLikedRepository));

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

    public static ViewModelFactory getInstance(Context context, Application application) {
        if (viewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (viewModelFactory == null) {
                    if (context != null) {
                        locationRepository = LocationRepository.getInstance(context, application);
                        viewModelFactory = new ViewModelFactory(locationRepository, application);
                    }

                    if (context != null && application != null) {
                        placeRepository = PlaceRepository.getInstance(context, application, locationRepository);
                        placeAutocompleteRepository = PlaceAutocompleteRepository.getInstance(context, application, locationRepository);
                    }

                    workmateRepository = WorkmateRepository.getInstance();
                    restaurantLikedRepository = RestaurantLikedRepository.getInstance();
                    favoriteRestaurantRepository = FavoriteRestaurantRepository.getInstance();
                }

            }
        }
        return viewModelFactory;
    }

    public <T extends ViewModel> T obtainViewModel(Class<T> modelClass) {
        return viewModelFactory.create(modelClass);
    }
}
