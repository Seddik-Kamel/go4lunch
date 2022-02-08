package com.example.go4lunch.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.ui.activity.HomeScreenActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

public abstract class GoogleMapBaseFragment extends Fragment implements OnMapReadyCallback {

    interface LocationResult {
        void makeSomethingAfterLocationResult();
    }

    private GoogleMap map;
    private boolean locationPermissionGranted;
    private static final int DEFAULT_ZOOM = 15;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private MapView mapView;
    private int resourceMapView;
    private int resourceLayoutView;

    abstract int getActionBarTitle();
    abstract int getResourceMapView();
    abstract int getResourceLayoutView();
    abstract LocationResult getLocationResult();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationPermissionGranted = false;
        setActionBarTitle();
        initResourceView();
        initResourceMapView();
        disableOnBackPressed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(resourceLayoutView, container, false);
        setFusedLocationProvider();
        configureMapView(savedInstanceState, view);
        launchMap(mapView);
        getLocationPermissionArgument();

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        updateLocationUi();
        moveCameraOnLocation(getLocationResult());
        onMyLocationButtonClick();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void setActionBarTitle() {
        if (getActivity() != null)
            ((HomeScreenActivity) getActivity()).setActionBarTitle(getActionBarTitle());
    }

    protected void disableOnBackPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
    }

    public GoogleMap getMap() {
        return map;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(boolean locationPermissionGranted) {
        this.locationPermissionGranted = locationPermissionGranted;
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
                    lastKnownLocation = null;
                    if (getActivity() != null)
                        ((HomeScreenActivity) getActivity()).getLocationPermission();
                }
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    public void moveCameraOnLocation(LocationResult callable) {//UI

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), DEFAULT_ZOOM)
                            );
                            callable.makeSomethingAfterLocationResult();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void setFusedLocationProvider() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    private void launchMap(MapView mapView) {//UI
        if (getActivity() != null)
            try {
                if (getActivity() != null)
                    MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

        mapView.getMapAsync(this);
    }

    private void initResourceMapView() {
        resourceMapView = getResourceMapView();
    }

    private void initResourceView() {
        resourceLayoutView = getResourceLayoutView();
    }

    private void configureMapView(@Nullable Bundle savedInstanceState, View view) {
        mapView = view.findViewById(resourceMapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
    }

    public void onMyLocationButtonClick() {
        map.setOnMyLocationButtonClickListener(() -> {
            moveCameraOnLocation(getLocationResult());
            return true;
        });
    }

    private void getLocationPermissionArgument() {
        Bundle bundle = getArguments();
        if (bundle != null)
            setLocationPermissionGranted(bundle.getBoolean(MapViewFragment.LOCATION_GRANTED));
    }
}
