package com.example.go4lunch.ui.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.model.RestaurantModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapViewModel extends ViewModel {
    private List<RestaurantModel> listRestaurants = new ArrayList<>();

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
                    RestaurantModel restaurantModel = new RestaurantModel(id_place, name, address, latLng);
                    listRestaurants.add(restaurantModel);
                }
            }
        }
    }

    @NonNull
    public List<Place.Field> getPlaceField() {
        List<Place.Field> placeField = Arrays.asList(Place.Field.ID, Place.Field.TYPES, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        return placeField;
    }

    /*private void setPermissionGranted() { // TODO à revoir le nom de la variable.
        if (getActivity() != null)
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationPermissionGranted = true;
            else
                ((HomeScreenActivity) getActivity()).getLocationPermission();
    }*/

    public List<RestaurantModel> getListRestaurants() {
        return listRestaurants;
    }

    public void setListRestaurants(List<RestaurantModel> listRestaurants) {
        this.listRestaurants = listRestaurants;
    }
}
