package com.example.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.R;
import com.example.go4lunch.utils.eventBus.LocationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MapViewFragment extends GoogleMapBaseFragment {

    public static final String LOCATION_GRANTED = "isLocationPermissionGranted";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    int getResourceLayout() {
        return R.layout.fragment_map_view;
    }


    @Subscribe
    public void onEvent(LocationEvent locationEvent) {
        this.onMapReady(getMap());
    }

    public static MapViewFragment newInstance(boolean locationPermissionGranted) {
        MapViewFragment fragment = new MapViewFragment();
        Bundle args = new Bundle();
        args.putBoolean(LOCATION_GRANTED, locationPermissionGranted);
        fragment.setArguments(args);
        return fragment;
    }
}