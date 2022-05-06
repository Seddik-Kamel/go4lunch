package com.example.go4lunch.ui.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.infrastructure.entity.LocationEntity;
import com.example.go4lunch.infrastructure.entity.PlaceEntity;
import com.example.go4lunch.infrastructure.repository.FirebaseRepository;
import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.model.PlaceModel;
import com.example.go4lunch.state.AutocompleteState;
import com.example.go4lunch.state.MainPageState;
import com.example.go4lunch.state.NearRestaurantUpdateState;
import com.example.go4lunch.usecase.AutocompleteUseCase;
import com.example.go4lunch.usecase.NearRestaurantUseCase;
import com.example.go4lunch.usecase.RestaurantLikedUseCase;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

    private ArrayList<PlaceModel> restaurantList = new ArrayList<>();
    private ArrayList<String> restaurantLikedList = new ArrayList<>();
    public final NearRestaurantUseCase nearRestaurantUseCase;
    public final AutocompleteUseCase autocompleteUseCase;
    public final RestaurantLikedUseCase restaurantLikedUseCase;
    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;

    private final MutableLiveData<MainPageState> _state = new MutableLiveData<>();
    public final LiveData<MainPageState> state = _state;

    public MainViewModel(NearRestaurantUseCase nearRestaurantUseCase, RestaurantLikedUseCase restaurantLikedUseCase,
                         AutocompleteUseCase autocompleteUseCase, LocationRepository locationRepository, PlaceRepository placeRepository) {
        this.nearRestaurantUseCase = nearRestaurantUseCase;
        this.restaurantLikedUseCase = restaurantLikedUseCase;
        this.autocompleteUseCase = autocompleteUseCase;
        this.locationRepository = locationRepository;
        this.placeRepository = placeRepository;
    }

    public void onLoadView() {
        restaurantLikedUseCase.observeForever(restaurantLikedState -> {
            restaurantLikedList = restaurantLikedState.getRestaurantModelArrayList();

        });

        nearRestaurantUseCase.observeForever(nearRestaurants -> {
            restaurantList = nearRestaurants.getRestaurantModelArrayList();
            updateLikedRestaurants(PlaceEntity.updateRestaurantEntity(nearRestaurants.getRestaurantModelArrayList()));
              _state.setValue(new NearRestaurantUpdateState(nearRestaurants.getCurrentLocation(), nearRestaurants.getRestaurantModelArrayList()));
        });
    }

    public void onLoadAutoComplete() {
        autocompleteUseCase.observeForever(autocomplete -> _state.setValue(new AutocompleteState(autocomplete.getRestaurantModelArrayList())));
    }

    public void onLocationPermissionsAccepted() {
        nearRestaurantUseCase.startService();
    }

    public ArrayList<PlaceModel> getRestaurantList() {
        return restaurantList;
    }

    public void stopLocationUpdate() {
        nearRestaurantUseCase.stopLocationUpdate();
    }

    public Location getLocation() {
        return nearRestaurantUseCase.getCurrentLocation();
    }

    public void onOpenAutocomplete(Context context) {
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, PlaceRepository.autocompleteField)
                .setCountry("FR")
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .build(context);
        _state.setValue(new AutocompleteState(intent));
    }

    public void onUpdateMapView(PlaceModel placeModel) {
        autocompleteUseCase.updateRestaurant(placeModel);
    }

    private void updateLikedRestaurants(@NonNull List<PlaceEntity> restaurantList) {
        for (PlaceEntity placeEntity : restaurantList) {
            if (restaurantLikedList.contains(placeEntity.getPlaceId() + FirebaseRepository.getUser().getUid())) {
                placeEntity.setMarkedColor(PlaceModel.MARKET_COLOR_RESTAURANT_LIKED);
            } else {
                placeEntity.setMarkedColor(PlaceModel.DEFAULT_MARKET_COLOR);
            }

            // update to local database
          // update(restaurantEntity);
        }
    }


    public LiveData<LocationEntity> getLastLocationLiveData() {
        return locationRepository.getLastLocationLiveData();
    }

    public void update(PlaceEntity placeEntity) {
        placeRepository.update(placeEntity);
    }
}
