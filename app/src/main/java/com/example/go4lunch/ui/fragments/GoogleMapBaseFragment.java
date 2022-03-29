package com.example.go4lunch.ui.fragments;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.go4lunch.ui.fragments.MapViewFragment.HUE_ORANGE;

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
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.viewmodel.MapViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.usecase.NearRestaurantUpdateState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public abstract class GoogleMapBaseFragment extends BaseFragment implements OnMapReadyCallback {

    public Location lastKnowLocation;
    private GoogleMap map;
    private MapViewModel mapViewModel;
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
        mapViewModel = ViewModelFactory.getInstance(requireContext(), getActivity().getApplication()).obtainViewModel(MapViewModel.class);
        mapViewModel.onLoadView();
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);
        mapView = view.findViewById(R.id.mapView);

        return view;
    }

    private final ActivityResultLauncher<String> permissions = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result && ActivityCompat.checkSelfPermission(requireActivity(), ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mapViewModel.onLocationPermissionsAccepted();
                    locationPermissionGranted = true;
                    updateLocationUi();
                } else {
                    Log.e("TAG", "onActivityResult: PERMISSION DENIED");
                }
            });

    public void render(NearRestaurantUpdateState nearRestaurantUpdateState) {
        if (nearRestaurantUpdateState != null) {
            lastKnowLocation = Objects.requireNonNull(nearRestaurantUpdateState.getCurrentLocation());
            mapViewModel.setRestaurantList(Objects.requireNonNull((nearRestaurantUpdateState).getRestaurantModelArrayList()));
            moveCameraOnPosition();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        updateLocationUi();
        mapViewModel.state.observe(requireActivity(), this::render);
    }

    public GoogleMap getMap() {
        return map;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        permissions.launch(ACCESS_FINE_LOCATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewModel.stopLocationUpdate();
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

    private void moveCameraOnPosition() {
        if (lastKnowLocation != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastKnowLocation.getLatitude(), lastKnowLocation.getLongitude()), DEFAULT_ZOOM)
            );
            addMarkers(mapViewModel.getRestaurantList());
        }
    }

    private void configureMapView(@Nullable Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void addMarkers(ArrayList<RestaurantModel> restaurantList) { //UI
        for (RestaurantModel restaurantModel : restaurantList) {
            getMap().addMarker(new MarkerOptions()
                    .title(restaurantModel.getName())
                    .position(restaurantModel.getLatLng())
                    .icon(BitmapDescriptorFactory.defaultMarker(HUE_ORANGE))
            );
        }
    }
}
