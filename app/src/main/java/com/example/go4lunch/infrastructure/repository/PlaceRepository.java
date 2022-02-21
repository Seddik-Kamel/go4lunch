package com.example.go4lunch.infrastructure.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.model.RestaurantModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceRepository extends LiveData<ArrayList<RestaurantModel>> {

    private static PlaceRepository placeRepository = null;
    FindCurrentPlaceRequest findCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.RATING,
            Place.Field.TYPES));

    Context context;
    PlacesClient placesClient;

    private PlaceRepository(Context context) {
        this.context = context.getApplicationContext();
        Places.initialize(context, BuildConfig.API_KEY);
        placesClient = Places.createClient(this.context);
    }

    public static PlaceRepository getInstance(Context context) {
        if (placeRepository == null) {
            synchronized (PlaceRepository.class) {
                if (placeRepository == null) {
                    placeRepository = new PlaceRepository(context.getApplicationContext());
                }
            }
        }
        return placeRepository;
    }

    @NonNull
    public List<Place.Field> getPlaceField() {
        List<Place.Field> placeField = Arrays.asList(Place.Field.ID, Place.Field.TYPES, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.PHOTO_METADATAS);
        return placeField;
    }

    @Override
    protected void onActive() {
        super.onActive();
    }

    public void findPlace() {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        ) {

            findPlacesWithPermissions();
        }
    }

    private void findPlacesWithPermissions() {
        @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                placesClient.findCurrentPlace(findCurrentPlaceRequest);

        placeResult.addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();
                        setValue(addRestaurants(likelyPlaces));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Place not found: " + e.getMessage());
                    }
                });
    }

    public boolean isRestaurants(List<Place.Type> listType) {
        return listType.contains(Place.Type.RESTAURANT) || listType.contains(Place.Type.FOOD);
    }

    public ArrayList<RestaurantModel> addRestaurants(FindCurrentPlaceResponse likelyPlaces) { //business logic
        final ArrayList<RestaurantModel> restaurantModelArrayList = new ArrayList<>();
        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
            List<Place.Type> listType = placeLikelihood.getPlace().getTypes();
            if (listType != null) {
                if (isRestaurants(listType)) {
                    restaurantModelArrayList.add(RestaurantModel.fromPlace(placeLikelihood.getPlace()));
                }
            }
        }

        return restaurantModelArrayList;
    }
}
