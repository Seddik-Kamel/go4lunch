package com.example.go4lunch.infrastructure.repository;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.model.RestaurantModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlaceRepository extends LiveData<ArrayList<RestaurantModel>> {

    private static PlaceRepository placeRepository = null;
    private final PlacesClient placesClient;
    private final RestaurantRepository restaurantRepository;

    private final List<Place.Field> requestFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS,
            Place.Field.TYPES);

    private final List<Place.Field> requestDetailFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.UTC_OFFSET,
            Place.Field.OPENING_HOURS,
            Place.Field.WEBSITE_URI,
            Place.Field.PHONE_NUMBER,
            Place.Field.TYPES);

    private final Context context;
    private final LocationRepository locationRepository;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private PlaceRepository(Context context, Application application, LocationRepository locationRepository) {
        this.context = context.getApplicationContext();
        this.restaurantRepository = new RestaurantRepository(application);
        Places.initialize(context, BuildConfig.API_KEY);
        placesClient = Places.createClient(this.context);
        this.locationRepository = locationRepository;
    }

    public static PlaceRepository getInstance(Context context, Application application, LocationRepository locationRepository) {
        if (placeRepository == null) {
            synchronized (PlaceRepository.class) {
                if (placeRepository == null) {
                    placeRepository = new PlaceRepository(context.getApplicationContext(), application, locationRepository);
                }
            }
        }
        return placeRepository;
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
                placesClient.findCurrentPlace(FindCurrentPlaceRequest.newInstance(requestFields));

        placeResult
                .continueWith(executor, this::convertAndFilterCurrentPlaceResponseToRestaurants)
                .continueWith(executor, this::retrievePhotoRestaurantsFromPhotoApi)
                .continueWith(executor, this::retrieveDetailsRestaurantsFromDetailsApi)
                .addOnCompleteListener(this::updateListeners);
    }

    private ArrayList<RestaurantModel> convertAndFilterCurrentPlaceResponseToRestaurants(Task<FindCurrentPlaceResponse> task) {
        final ArrayList<RestaurantModel> restaurants = new ArrayList<>();
        if (task.isSuccessful()) {
            filterResults(task.getResult(), restaurants);

        } else {
            logException(task.getException());
        }

        return restaurants;
    }

    private void logException(Exception exception) {
        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            Log.e("TAG", "API Exception: " + apiException.getStatusCode());
        } else {
            Log.e("TAG", "Unknonw exception: " + exception);
        }
    }

    private void updateListeners(Task<ArrayList<RestaurantModel>> task) {
        setValue(task.getResult());

    }

    private void filterResults(FindCurrentPlaceResponse response, ArrayList<RestaurantModel> restaurants) {
        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
            final List<Place.Type> types = Objects.requireNonNull(placeLikelihood.getPlace().getTypes());
            if (types.contains(Place.Type.RESTAURANT) || types.contains(Place.Type.FOOD)) {
                restaurants.add(RestaurantModel.fromPlace(placeLikelihood.getPlace(), locationRepository.getCurrentLocation()));
            }
        }
    }

    private ArrayList<RestaurantModel> retrieveDetailsRestaurantsFromDetailsApi(Task<ArrayList<RestaurantModel>> continuation) {
        ArrayList<RestaurantModel> restaurants = continuation.getResult();
        restaurantRepository.deleteAll();
        for (RestaurantModel restaurant : restaurants) {
            tryFetchPlaceDetails(restaurant);
        }
        return restaurants;
    }

    private void tryFetchPlaceDetails(RestaurantModel restaurant) {
        Task<FetchPlaceResponse> fetchPlaceResponseTask = placesClient.fetchPlace(FetchPlaceRequest.builder(restaurant.getPlaceId(), requestDetailFields).build());
        try {
            FetchPlaceResponse placeResponse = Tasks.await(fetchPlaceResponseTask);
            updateRestaurantWithPlaceResponse(restaurant, placeResponse);
        } catch (Exception exception) {
            logException(exception);
        }
    }

    private void updateRestaurantWithPlaceResponse(RestaurantModel restaurant, FetchPlaceResponse placeResponse) {
        Place place = placeResponse.getPlace();
        restaurant.setWebSitUrl(place.getWebsiteUri() != null ? place.getWebsiteUri().toString() : null);
        restaurant.setPhoneNumber(place.getPhoneNumber());
        final String unknownInformation = context.getString(R.string.unknown_information);
        restaurant.setIsOpen(place.isOpen() != null ? place.isOpen() ? context.getString(R.string.place_open) : context.getString(R.string.place_close) : unknownInformation);

        RestaurantEntity restaurantEntity = RestaurantEntity.updateData(restaurant);
        restaurantRepository.insert(restaurantEntity);
    }

    @NonNull
    private ArrayList<RestaurantModel> retrievePhotoRestaurantsFromPhotoApi(Task<ArrayList<RestaurantModel>> continuation) {
        ArrayList<RestaurantModel> restaurants = continuation.getResult();
        for (RestaurantModel restaurant : restaurants)
            if (restaurant.getPhotoMetadata() != null)
                tryFetchRestaurantImage(restaurant);

        return restaurants;
    }

    private void tryFetchRestaurantImage(RestaurantModel restaurant) {
        Task<FetchPhotoResponse> fetchPhotoResponseTask = placesClient.fetchPhoto(FetchPhotoRequest.builder(restaurant.getPhotoMetadata()).build());
        try {
            FetchPhotoResponse placeResponse = Tasks.await(fetchPhotoResponseTask);
            restaurant.setBitmap(placeResponse.getBitmap());
        } catch (Exception exception) {
            logException(exception);
        }
    }

    public PlacesClient getPlacesClient() {
        return placesClient;
    }
}
