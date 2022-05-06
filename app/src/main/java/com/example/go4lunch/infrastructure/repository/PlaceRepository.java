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
import com.example.go4lunch.infrastructure.dao.RestaurantDao;
import com.example.go4lunch.infrastructure.database.GoLunchDatabase;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.state.LocationState;
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

    protected static PlaceRepository placeRepository = null;
    protected final PlacesClient placesClient;

    protected final Context context;
    protected final LocationRepository locationRepository;
    protected final Executor executor = Executors.newSingleThreadExecutor();
    private final RestaurantDao restaurantDao;
    private LocationState locationState;

    public static final List<Place.Field> autocompleteField = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS,
            Place.Field.TYPES);


    private final List<Place.Field> requestFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS,
            Place.Field.TYPES);

    protected final List<Place.Field> requestDetailFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.UTC_OFFSET,
            Place.Field.OPENING_HOURS,
            Place.Field.WEBSITE_URI,
            Place.Field.PHONE_NUMBER,
            Place.Field.TYPES);

    protected PlaceRepository(Context context, Application application, LocationRepository locationRepository) {
        GoLunchDatabase goLunchDatabase = GoLunchDatabase.getDatabase(application);
        this.restaurantDao = goLunchDatabase.restaurantDao();
        this.context = context.getApplicationContext();
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

    public void findPlace(LocationState locationState) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        ) {

            if (locationState.isNewLocation()) {
                findPlacesWithPermissions();
                this.locationState = locationState;
            } else
                GoLunchDatabase.databaseWriteExecutor.execute(() -> postValue(RestaurantEntity.updateRestaurantModel(getAllRestaurants())));
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

    protected void logException(Exception exception) {
        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            Log.e("TAG", "API Exception: " + apiException.getStatusCode());
        } else {
            Log.e("TAG", "Unknonw exception: " + exception);
        }
    }

    protected void updateListeners(Task<ArrayList<RestaurantModel>> task) {
        if (task.isSuccessful())
            setValue(task.getResult());
    }

    private void filterResults(FindCurrentPlaceResponse response, ArrayList<RestaurantModel> restaurants) {
        for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
            final List<Place.Type> types = Objects.requireNonNull(placeLikelihood.getPlace().getTypes());
            if (types.contains(Place.Type.RESTAURANT) || types.contains(Place.Type.FOOD)) {
                restaurants.add(RestaurantModel.fromPlace(placeLikelihood.getPlace(), locationState.getCurrentLocation()));
            }
        }
    }

    protected ArrayList<RestaurantModel> retrieveDetailsRestaurantsFromDetailsApi(Task<ArrayList<RestaurantModel>> continuation) {
        ArrayList<RestaurantModel> restaurants = continuation.getResult();

        // Delete from local database.
        deleteAll();

        for (RestaurantModel restaurant : restaurants) {
            tryFetchPlaceDetails(restaurant);
        }
        return restaurants;
    }

    protected void tryFetchPlaceDetails(RestaurantModel restaurant) {
        Task<FetchPlaceResponse> fetchPlaceResponseTask = placesClient.fetchPlace(FetchPlaceRequest.builder(restaurant.getPlaceId(), requestDetailFields).build());
        try {
            FetchPlaceResponse placeResponse = Tasks.await(fetchPlaceResponseTask);
            updateRestaurantWithPlaceResponse(restaurant, placeResponse);
        } catch (Exception exception) {
            logException(exception);
        }
    }

    protected void updateRestaurantWithPlaceResponse(RestaurantModel restaurant, FetchPlaceResponse placeResponse) {
        Place place = placeResponse.getPlace();
        restaurant.setWebSitUrl(place.getWebsiteUri() != null ? place.getWebsiteUri().toString() : null);
        restaurant.setPhoneNumber(place.getPhoneNumber());
        final String unknownInformation = context.getString(R.string.unknown_information);
        restaurant.setIsOpen(place.isOpen() != null ? place.isOpen() ? context.getString(R.string.place_open) : context.getString(R.string.place_close) : unknownInformation);

       //Insert to local database
        RestaurantEntity restaurantEntity = RestaurantEntity.updateData(restaurant);
        insert(restaurantEntity);
    }

    @NonNull
    protected ArrayList<RestaurantModel> retrievePhotoRestaurantsFromPhotoApi(Task<ArrayList<RestaurantModel>> continuation) {
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

    @NonNull
    protected Task<FetchPhotoResponse> generateFetchPhotoTask(RestaurantModel restaurant) {
        return placesClient.fetchPhoto(
                FetchPhotoRequest.builder(restaurant.getPhotoMetadata()).build());
    }

    // Locale database
    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantDao.getAlphabetizedRestaurant();
    }

    public LiveData<RestaurantEntity> getRestaurant(String placeId) {
        return restaurantDao.getRestaurant(placeId);
    }

    public void insert(RestaurantEntity restaurantEntity) {
        GoLunchDatabase.databaseWriteExecutor.execute(() -> restaurantDao.insert(restaurantEntity));
    }

    public void deleteAll() {
        GoLunchDatabase.databaseWriteExecutor.execute(restaurantDao::deleteAll);
    }

    public void update(RestaurantEntity restaurantEntity){
        GoLunchDatabase.databaseWriteExecutor.execute(() -> restaurantDao.update(restaurantEntity));
    }
}
