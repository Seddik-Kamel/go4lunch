package com.example.go4lunch.ui.fragments;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.go4lunch.R;
import com.example.go4lunch.infrastructure.entity.LocationEntity;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.state.AutocompleteState;
import com.example.go4lunch.state.MainPageState;
import com.example.go4lunch.state.NearRestaurantUpdateState;
import com.example.go4lunch.ui.viewmodel.MainViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public abstract class GoogleMapBaseFragment extends BaseFragment implements OnMapReadyCallback {

    public Location lastKnowLocation;
    private GoogleMap map;
    private MainViewModel mainViewModel;
    private static final int DEFAULT_ZOOM = 15;

    public MapView mapView;
    public boolean locationPermissionGranted = false;

    abstract int getActionBarTitle();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mapView != null) {
            configureMapView(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainViewModel = ViewModelFactory.getInstance(requireContext(), getActivity().getApplication()).obtainViewModel(MainViewModel.class);
        mainViewModel.onLoadAutoComplete();
        mainViewModel.onLoadView();
        mainViewModel.getLastLocationLiveData().observe(getActivity(), this::locationRender);

        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = view.findViewById(R.id.mapView);

        return view;
    }

    private final ActivityResultLauncher<String> permissions = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result && ActivityCompat.checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mainViewModel.onLocationPermissionsAccepted();
                    locationPermissionGranted = true;
                    updateLocationUi();
                } else {
                    Log.e("TAG", "onActivityResult: PERMISSION DENIED");
                }
            });

    private void locationRender(LocationEntity locationEntity) {
        if (locationEntity != null) {
            Location location = new Location("");
            location.setLongitude(locationEntity.getLongitude());
            location.setLatitude(locationEntity.getLatitude());
            lastKnowLocation = location;
        }
    }

    public void render(MainPageState mainPageState) {
        if (mainPageState instanceof NearRestaurantUpdateState) {
            lastKnowLocation = ((NearRestaurantUpdateState) mainPageState).getCurrentLocation();
            moveCameraOnPosition(((NearRestaurantUpdateState) mainPageState).getRestaurantModelArrayList());
        }

        if (mainPageState instanceof AutocompleteState) {
            moveCameraOnThiSRestaurant(((AutocompleteState) mainPageState).getRestaurantModelArrayList());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        updateLocationUi();
        mainViewModel.state.observe(this, this::render);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        permissions.launch(ACCESS_FINE_LOCATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        mainViewModel.stopLocationUpdate();
    }

    @SuppressWarnings("MissingPermission")
    public void updateLocationUi() {//UI
        if (map != null) {
            try {
                if (locationPermissionGranted) {
                    map.setMyLocationEnabled(true);
                    map.getUiSettings().setMyLocationButtonEnabled(true);

                } else {
                    map.setMyLocationEnabled(false);
                    map.getUiSettings().setMyLocationButtonEnabled(false);
                    lastKnowLocation = null;
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    public GoogleMap getMap() {
        return map;
    }

    private void moveCameraOnPosition(ArrayList<RestaurantModel> restaurants) {
        if (lastKnowLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude()), DEFAULT_ZOOM)
            );
            addMarkers(restaurants);
        }
    }

    private void moveCameraOnThiSRestaurant(ArrayList<RestaurantModel> restaurants) {
        if (lastKnowLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    restaurants.get(0).getLatLng(), DEFAULT_ZOOM)
            );
            addMarkers(restaurants);
        }
    }

    private void configureMapView(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void addMarkers(ArrayList<RestaurantModel> restaurantList) {
        for (RestaurantModel restaurantModel : restaurantList) {
            getMap().addMarker(new MarkerOptions()
                    .title(restaurantModel.getName())
                    .position(restaurantModel.getLatLng())
                    .icon(BitmapDescriptorFactory.defaultMarker(restaurantModel.getMarkedColor()))
            );
        }
    }
}
