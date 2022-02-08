package com.example.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;

public class RestaurantModel {

    private long id;
    private String id_place;
    private String name;
    private String address;
    private LatLng latLng;

    public RestaurantModel(String id_place, String name, String address, LatLng latLng) {
        this.id_place = id_place;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_place() {
        return id_place;
    }

    public void setId_place(String id_place) {
        this.id_place = id_place;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
