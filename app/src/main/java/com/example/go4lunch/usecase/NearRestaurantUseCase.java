package com.example.go4lunch.usecase;

import android.location.Location;

import androidx.lifecycle.MediatorLiveData;

import com.example.go4lunch.infrastructure.entity.LocationEntity;
import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.state.LocationState;
import com.example.go4lunch.state.NearRestaurantUpdateState;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class NearRestaurantUseCase extends MediatorLiveData<NearRestaurantUpdateState> {

    private Location currentLocation;

    private ArrayList<RestaurantModel> restaurantModelArrayList;
    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;
    private  LocationState locationState;

    public NearRestaurantUseCase(LocationRepository locationRepository, PlaceRepository placeRepository) {
        this.locationRepository = locationRepository;
        this.placeRepository = placeRepository;

        addSource(locationRepository, (source) ->{
            locationState = source;
            currentLocation = locationState.getCurrentLocation();
            placeRepository.findPlace(locationState);
        });


        addSource(placeRepository, (source) -> {
            if (!source.equals(restaurantModelArrayList)) {
                restaurantModelArrayList = source;
                notifyObserver();
            }
        });
    }

    public void notifyObserver() {
        if (locationState != null && restaurantModelArrayList != null)
            setValue(new NearRestaurantUpdateState(locationState.getCurrentLocation(), restaurantModelArrayList));
    }

    public void startService() {
        locationRepository.startService();
    }

    public PlacesClient getPlacesClient() {
        return placeRepository.getPlacesClient();
    }

    public void stopLocationUpdate() {
        locationRepository.stopLocationUpdate();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public LocationEntity getSavedLocation() {
        return locationRepository.getLastLocationEntity();
    }
}
