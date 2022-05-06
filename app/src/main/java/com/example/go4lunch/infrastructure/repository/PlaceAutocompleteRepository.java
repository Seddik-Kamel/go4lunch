package com.example.go4lunch.infrastructure.repository;

import android.app.Application;
import android.content.Context;

import com.example.go4lunch.R;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.model.RestaurantModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;

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

    /**
     *
     * I rewrite the same code as in PlaceRepository because in the case of autocomplete there is no need to save in the local database
     */

    //TODO Partie à revoir.

    protected ArrayList<RestaurantModel> retrieveDetailsPlaceAutocomplete(Task<ArrayList<RestaurantModel>> continuation) {
        ArrayList<RestaurantModel> restaurants = continuation.getResult();
        for (RestaurantModel restaurant : restaurants) {
            tryFetchPlaceDetailsAutocomplete(restaurant);
        }
        return restaurants;
    }

    protected void tryFetchPlaceDetailsAutocomplete(RestaurantModel restaurant) {
        Task<FetchPlaceResponse> fetchPlaceResponseTask = placesClient.fetchPlace(FetchPlaceRequest.builder(restaurant.getPlaceId(), requestDetailFields).build());
        try {
            FetchPlaceResponse placeResponse = Tasks.await(fetchPlaceResponseTask);
            updateRestaurantWithPlaceResponseAutocomplete(restaurant, placeResponse);
        } catch (Exception exception) {
            logException(exception);
        }
    }

    protected void updateRestaurantWithPlaceResponseAutocomplete(RestaurantModel restaurant, FetchPlaceResponse placeResponse) {
        Place place = placeResponse.getPlace();
        restaurant.setWebSitUrl(place.getWebsiteUri() != null ? place.getWebsiteUri().toString() : null);
        restaurant.setPhoneNumber(place.getPhoneNumber());
        final String unknownInformation = context.getString(R.string.unknown_information);
        restaurant.setIsOpen(place.isOpen() != null ? place.isOpen() ? context.getString(R.string.place_open) : context.getString(R.string.place_close) : unknownInformation);
    }

    public void updateRestaurant(RestaurantModel restaurantModel) {
        if (restaurantModel.getPhotoMetadata() != null) {
            generateFetchPhotoTask(restaurantModel)
                    .continueWith(executor, result -> {
                        restaurantModel.setBitmap(result.getResult().getBitmap());
                        return new ArrayList<>(Collections.singletonList(restaurantModel));
                    })
                    .continueWith(executor, this::retrieveDetailsPlaceAutocomplete)
                    .addOnCompleteListener(this::updateListeners);
        }
    }
}
