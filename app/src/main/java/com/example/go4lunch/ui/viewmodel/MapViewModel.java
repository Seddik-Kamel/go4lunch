package com.example.go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.usecase.NearRestaurantUpdateState;
import com.example.go4lunch.usecase.NearRestaurantUseCase;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

public class MapViewModel extends ViewModel {


    private ArrayList<RestaurantModel> restaurantList = new ArrayList<>();
    public final NearRestaurantUseCase nearRestaurantUseCase;
    private final MutableLiveData<NearRestaurantUpdateState> _state = new MutableLiveData<>();
    public final LiveData<NearRestaurantUpdateState> state = _state;

    public MapViewModel(NearRestaurantUseCase nearRestaurantUseCase) {
        this.nearRestaurantUseCase = nearRestaurantUseCase;
    }

    public void onLoadView() {
        nearRestaurantUseCase.observeForever(nearRestaurants -> _state.setValue(new NearRestaurantUpdateState(nearRestaurants.getCurrentLocation(), nearRestaurants.getRestaurantModelArrayList())));
    }

    public void onLocationPermissionsAccepted() {
        nearRestaurantUseCase.startService();
    }


    public ArrayList<RestaurantModel> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(ArrayList<RestaurantModel> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public PlacesClient getPlaceClient(){
      return this.nearRestaurantUseCase.getPlacesClient();
    }
    public void stopLocationUpdate(){
        nearRestaurantUseCase.stopLocationUpdate();
    }
}
