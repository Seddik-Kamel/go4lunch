package com.example.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.viewmodel.MapViewModel;
import com.example.go4lunch.utils.eventBus.LocationEvent;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MapViewFragment extends GoogleMapBaseFragment {

    public static final String LOCATION_GRANTED = "isLocationPermissionGranted";
    public static final float HUE_ORANGE = BitmapDescriptorFactory.HUE_ORANGE;
    private PlacesClient placesClient;
    private MapViewModel mapViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializePlace();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null)
            mapViewModel = new ViewModelProvider(getActivity()).get(MapViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    int getActionBarTitle() {
        return R.string.fragment_title_map;
    }

    @Override
    int getResourceMapView() {
        return R.id.mapView;
    }

    @Override
    int getResourceLayoutView() {
        return R.layout.map_view_fragment;
    }

    @Override
    LocationResult getLocationResult() {
        return this::showRestaurantsOnMap;
    }

    @Subscribe
    public void onEvent(LocationEvent locationEvent) {
        setLocationPermissionGranted(locationEvent.locationPermissionGranted);
        this.onMapReady(getMap());
    }

    public static MapViewFragment newInstance(boolean locationPermissionGranted) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putBoolean(LOCATION_GRANTED, locationPermissionGranted);
        fragment.setArguments(args);
        return fragment;
    }

    private void initializePlace() {
        if (getActivity() != null)
            Places.initialize(getActivity().getApplicationContext(), BuildConfig.API_KEY);
        placesClient = Places.createClient(getActivity());
    }

    private void showRestaurantsOnMap() {//UI
        if (getMap() == null) {
            return;
        }
        if (isLocationPermissionGranted()) {
            FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(mapViewModel.getPlaceField());
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);

            placeResult.addOnCompleteListener(task -> { // after get place
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();
                    mapViewModel.addRestaurantsToList(likelyPlaces);
                    addMarkers();
                }
            });
        }
    }

    private void addMarkers() { //UI
        for (RestaurantModel restaurantModel : mapViewModel.getListRestaurants()) {
            getMap().addMarker(new MarkerOptions()
                    .title(restaurantModel.getName())
                    .position(restaurantModel.getLatLng())
                    .icon(BitmapDescriptorFactory.defaultMarker(HUE_ORANGE))
            );
        }
    }
}