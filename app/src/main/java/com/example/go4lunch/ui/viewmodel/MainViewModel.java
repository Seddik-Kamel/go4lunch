package com.example.go4lunch.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.infrastructure.entity.LocationEntity;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.infrastructure.repository.RestaurantRepository;
import com.example.go4lunch.model.RestaurantModel;
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


    private ArrayList<RestaurantModel> restaurantList = new ArrayList<>();
    private ArrayList<String> restaurantLikedList = new ArrayList<>();

    public final NearRestaurantUseCase nearRestaurantUseCase;
    public final AutocompleteUseCase autocompleteUseCase;
    public final RestaurantLikedUseCase restaurantLikedUseCase;
    protected final RestaurantRepository restaurantRepository;
    protected final LocationRepository locationRepository;

    private final MutableLiveData<MainPageState> _state = new MutableLiveData<>();
    public final LiveData<MainPageState> state = _state;

    public MainViewModel(NearRestaurantUseCase nearRestaurantUseCase, RestaurantLikedUseCase restaurantLikedUseCase,
                         AutocompleteUseCase autocompleteUseCase, Application application, LocationRepository locationRepository) {
        this.nearRestaurantUseCase = nearRestaurantUseCase;
        this.restaurantLikedUseCase = restaurantLikedUseCase;
        this.autocompleteUseCase = autocompleteUseCase;
        this.restaurantRepository = new RestaurantRepository(application);
        this.locationRepository = locationRepository;
    }

    public void onLoadView() {
        restaurantLikedUseCase.observeForever(restaurantLikedState -> {
            restaurantLikedList = restaurantLikedState.getRestaurantModelArrayList();
        });

        nearRestaurantUseCase.observeForever(nearRestaurants -> {
            restaurantList = nearRestaurants.getRestaurantModelArrayList();
            updateLikedRestaurants(nearRestaurants.getRestaurantModelArrayList());
            insertRestaurant(restaurantList);
         //  _state.setValue(new NearRestaurantUpdateState(nearRestaurants.getCurrentLocation(), nearRestaurants.getRestaurantModelArrayList()));
        });
    }

    public void onLoadAutoComplete() {
        autocompleteUseCase.observeForever(autocomplete -> _state.setValue(new AutocompleteState(autocomplete.getRestaurantModelArrayList())));
    }

    public void onLocationPermissionsAccepted() {
        nearRestaurantUseCase.startService();
    }

    public ArrayList<RestaurantModel> getRestaurantList() {
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

    public void onUpdateMapView(RestaurantModel restaurantModel) {
        autocompleteUseCase.updateRestaurant(restaurantModel);
    }

    private void updateLikedRestaurants(@NonNull ArrayList<RestaurantModel> restaurantList) {
        for (RestaurantModel restaurantModel : restaurantList) {
            if (restaurantLikedList.contains(restaurantModel.getPlaceId())) {
                restaurantModel.setMarkedColor(RestaurantModel.MARKET_COLOR_RESTAURANT_LIKED);
            } else {
                restaurantModel.setMarkedColor(RestaurantModel.DEFAULT_MARKET_COLOR);
            }
        }
    }

    public void insertRestaurant(ArrayList<RestaurantModel> restaurantEntities) {
        for (RestaurantModel restaurantModel : restaurantEntities) {
            restaurantRepository.insert(RestaurantEntity.updateData(restaurantModel));
        }
    }

    public LiveData<List<RestaurantEntity>> getAllRestaurant() {
        return restaurantRepository.getAllRestaurants();
    }

    public LiveData<LocationEntity> getLastLocationLiveData(){
        return locationRepository.getLastLocationLiveData();
    }
}
