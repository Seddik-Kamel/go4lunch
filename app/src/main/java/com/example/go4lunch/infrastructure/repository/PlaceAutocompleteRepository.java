package com.example.go4lunch.infrastructure.repository;

import android.app.Application;
import android.content.Context;

import com.example.go4lunch.model.RestaurantModel;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlaceAutocompleteRepository extends PlaceRepository {

    private static PlaceAutocompleteRepository placeAutocompleteRepository;

    public PlaceAutocompleteRepository(Context context, Application application, LocationRepository locationRepository) {
        super(context, application, locationRepository);
    }

    public static PlaceAutocompleteRepository getInstance(Context context, Application application, LocationRepository locationRepository) {
        if (placeAutocompleteRepository == null) {
            synchronized (PlaceRepository.class) {
                if (placeAutocompleteRepository == null) {
                    placeAutocompleteRepository = new PlaceAutocompleteRepository(context, application, locationRepository);
                }
            }
        }
        return placeAutocompleteRepository;
    }

    public static List<Place.Field> autocompleteField = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS,
            Place.Field.TYPES);

    public void updateRestaurant(RestaurantModel restaurantModel) {
        if (restaurantModel.getPhotoMetadata() != null) {
            generateFetchPhotoTask(restaurantModel)
                    .continueWith(executor, result -> {
                        restaurantModel.setBitmap(result.getResult().getBitmap());
                        return new ArrayList<>(Collections.singletonList(restaurantModel));
                    })
                    .continueWith(executor, this::retrieveDetailsRestaurantsFromDetailsApi)
                    .addOnCompleteListener(this::updateListeners);
        }
    }
}
