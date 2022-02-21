package com.example.go4lunch.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.infrastructure.repository.LocationRepository;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.usecase.NearRestaurantUpdateState;
import com.example.go4lunch.usecase.NearRestaurantUseCase;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapViewModel extends ViewModel {

    private List<RestaurantModel> listRestaurants = new ArrayList<>();
    public double lastDeviceLocation = 0;
    public LocationRepository locationRepository;
    public NearRestaurantUseCase nearRestaurantUseCase;
    private final MutableLiveData<NearRestaurantUpdateState> _state = new MutableLiveData<>();
    public LiveData<NearRestaurantUpdateState> state = _state;

    public MapViewModel(NearRestaurantUseCase nearRestaurantUseCase){
        this.nearRestaurantUseCase = nearRestaurantUseCase;
    }

    public void onLoadView(){
        nearRestaurantUseCase.observeForever(nearRestaurants -> {
           // updateDistanceOfRestaurants(nearRestaurants.getCurrentLocation(), nearRestaurants.getRestaurantList());
            int debug = 0;
            _state.setValue(new NearRestaurantUpdateState(nearRestaurants.getCurrentLocation(), nearRestaurants.getRestaurantModelArrayList()));
        });
    }

    public boolean isARestaurant(List<Place.Type> listType) {
        return listType.contains(Place.Type.RESTAURANT) || listType.contains(Place.Type.FOOD);
    }

    public void addRestaurantsToList(FindCurrentPlaceResponse likelyPlaces) { //business logic
        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
            List<Place.Type> listType = placeLikelihood.getPlace().getTypes();
            if (listType != null) {
                if (isARestaurant(listType)) {
                    String id_place = placeLikelihood.getPlace().getId();
                    String name = placeLikelihood.getPlace().getName();
                    String address = placeLikelihood.getPlace().getAddress();
                    LatLng latLng = placeLikelihood.getPlace().getLatLng();
                   // OpeningHours openingHours = placeLikelihood.getPlace().getOpeningHours();
                    List<PhotoMetadata> photoMetadata = placeLikelihood.getPlace().getPhotoMetadatas();
                    RestaurantModel restaurantModel = new RestaurantModel(id_place, name, address, latLng, photoMetadata);
                    listRestaurants.add(restaurantModel);
                }
            }
        }
    }

    @NonNull
    public List<Place.Field> getPlaceField() {
        List<Place.Field> placeField = Arrays.asList(Place.Field.ID, Place.Field.TYPES, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS);
        return placeField;
    }


    public List<RestaurantModel> getListRestaurants() {
        return listRestaurants;
    }

    public void setListRestaurants(List<RestaurantModel> listRestaurants) {
        this.listRestaurants = listRestaurants;
    }

    public void resetList(){
        listRestaurants.clear();
    }

    public void onLocationPermissionsAccepted() {
        nearRestaurantUseCase.startService();
    }
}
