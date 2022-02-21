package com.example.go4lunch.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;
import java.util.Objects;

public class RestaurantModel {

    private long id;
    private String placeId;
    private String name;
    public double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double longitude;
    private String address;
    private LatLng latLng;
    private List<PhotoMetadata>  photoMetadata;



    public RestaurantModel(){

    }

    public RestaurantModel(String placeId, String name, String address, LatLng latLng, List<PhotoMetadata> photoMetadata) {
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.latLng = latLng;
        this.photoMetadata = photoMetadata;
    }

    public static RestaurantModel fromPlace(Place place) {
        RestaurantModel restaurant = new RestaurantModel();
        restaurant.setPlaceId(place.getId());
        restaurant.setName(place.getName());
        restaurant.setAddress(place.getAddress());
       // restaurant.setRating(place.getRating() != null ? place.getRating() : 5); //TODO
        restaurant.setLatitude(Objects.requireNonNull(place.getLatLng()).latitude);
        restaurant.setLongitude(place.getLatLng().longitude);
        //restaurant.setDistance(determineUserDistance(place.getLatLng(), currentLatitude, currentLongitude));

        return restaurant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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


    public List<PhotoMetadata> getPhotoMetadata() {
        return photoMetadata;
    }

    public void setPhotoMetadata(List<PhotoMetadata> photoMetadata) {
        this.photoMetadata = photoMetadata;
    }
}
