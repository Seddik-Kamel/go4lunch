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
import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.model.PlaceModel;
import com.example.go4lunch.state.AutocompleteState;
import com.example.go4lunch.state.MainPageState;
import com.example.go4lunch.state.NearRestaurantUpdateState;
import com.example.go4lunch.state.RestaurantLikedState;
import com.example.go4lunch.usecase.AutocompleteUseCase;
import com.example.go4lunch.usecase.NearRestaurantUseCase;
import com.example.go4lunch.usecase.RestaurantLikedUseCase;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    private ArrayList<PlaceModel> restaurantList = new ArrayList<>();
    private ArrayList<PlaceEntity> restaurantLikedList = new ArrayList<>();
    public final NearRestaurantUseCase nearRestaurantUseCase;
    public final AutocompleteUseCase autocompleteUseCase;
    public final RestaurantLikedUseCase restaurantLikedUseCase;
    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;

    private final MutableLiveData<MainPageState> _state = new MutableLiveData<>();
    public final LiveData<MainPageState> state = _state;

    private final MutableLiveData<RestaurantLikedState> _likedRestaurantState = new MutableLiveData<>();
    public final LiveData<RestaurantLikedState> likedRestaurantState = _likedRestaurantState;


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

            _state.setValue(new NearRestaurantUpdateState(updateLikedRestaurants(restaurantList)));
        });

        nearRestaurantUseCase.observeForever(nearRestaurants -> {
            restaurantList = nearRestaurants.getRestaurantModelArrayList();
            _state.setValue(new NearRestaurantUpdateState(nearRestaurants.getCurrentLocation(), updateLikedRestaurants(nearRestaurants.getRestaurantModelArrayList())));
        });
    }

    public void onLoadAutoComplete() {
        autocompleteUseCase.observeForever(autocomplete -> _state.setValue(new AutocompleteState(autocomplete.getRestaurantModelArrayList())));
    }

    public void onLoadViewLikedPlace() {
        restaurantLikedUseCase.observeForever(restaurantLikedState -> _likedRestaurantState.setValue(new RestaurantLikedState(restaurantLikedState.getRestaurantModelArrayList())));
    }

    public void onLocationPermissionsAccepted() {
        nearRestaurantUseCase.startService();
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

    private ArrayList<PlaceModel> updateLikedRestaurants(@NonNull ArrayList<PlaceModel> restaurantList) {

        if (restaurantLikedList.isEmpty()) {
            for (PlaceModel placeModel : restaurantList) {
                placeModel.setMarkedColor(PlaceModel.DEFAULT_MARKET_COLOR);
            }
        } else {
            String placeIdLiked = restaurantLikedList.get(0).getPlaceId();
            for (PlaceModel placeModel : restaurantList) {
                if (placeIdLiked.equals(placeModel.getPlaceId())) {
                    placeModel.setMarkedColor(PlaceModel.MARKET_COLOR_RESTAURANT_LIKED);
                } else {
                    placeModel.setMarkedColor(PlaceModel.DEFAULT_MARKET_COLOR);
                }
            }

            ArrayList<PlaceEntity> placeEntities = PlaceEntity.updateRestaurantEntity(restaurantList);

            for (PlaceEntity placeEntity : placeEntities) {
                update(placeEntity);
            }
        }


        return restaurantList;

    }

    public LiveData<LocationEntity> getLastLocationLiveData() {
        return locationRepository.getLastLocationLiveData();
    }

    public void update(PlaceEntity placeEntity) {
        placeRepository.update(placeEntity);
    }
}
