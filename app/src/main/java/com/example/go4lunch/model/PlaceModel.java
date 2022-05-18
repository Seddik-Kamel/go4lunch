package com.example.go4lunch.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;

import java.util.Objects;

public class PlaceModel {

    private long id;
    private String placeId;
    private String currentUser;
    private String name;
    public double latitude;
    public double longitude;
    private String address;
    private LatLng latLng;
    private float rating;
    private float markedColor;
    private String isOpen;
    private String phoneNumber;
    private String webSitUri;
    private Bitmap bitmap;
    private byte[] bytesImage;
    private Integer userRatingTotal;
    private int userDistance;
    private PhotoMetadata photoMetadata;
    private boolean isLiked;

    public static final float DEFAULT_MARKET_COLOR = BitmapDescriptorFactory.HUE_ORANGE;
    public static final float MARKET_COLOR_RESTAURANT_LIKED = BitmapDescriptorFactory.HUE_GREEN;

    public PlaceModel() {

    }

    public static PlaceModel fromPlace(Place place, Location currentLocation) {
        PlaceModel restaurant = new PlaceModel();
        restaurant.setPlaceId(place.getId());
        restaurant.setName(place.getName());
        restaurant.setUserRatingTotal(place.getUserRatingsTotal());
        restaurant.setUserDistance(determineUserDistance(Objects.requireNonNull(place.getLatLng()), currentLocation.getLatitude(), currentLocation.getLongitude()));
        restaurant.setAddress(place.getAddress());
        restaurant.setRating(Objects.requireNonNull(place.getRating()).floatValue());
        restaurant.setMetadata(place.getPhotoMetadatas() != null ? place.getPhotoMetadatas().get(0) : null);
        restaurant.setLatLng(Objects.requireNonNull(place.getLatLng()));
        return restaurant;
    }

    private static int determineUserDistance(LatLng place, double currentLatitude, double currentLongitude) {
        Location currentLocation = new Location("CURRENT LOCATION");
        Location restaurantLocation = new Location("RESTAURANT LOCATION");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);
        restaurantLocation.setLatitude(place.latitude);
        restaurantLocation.setLongitude(place.longitude);
        return (int) currentLocation.distanceTo(restaurantLocation);
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String isOpen() {
        return isOpen;
    }

    public void setIsOpen(String open) {
        isOpen = open;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebSitUri() {
        return webSitUri;
    }

    public void setWebSitUrl(String webSitUri) {
        this.webSitUri = webSitUri;
    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            return BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);
        } else {
            return bitmap;
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public PhotoMetadata getPhotoMetadata() {
        return photoMetadata;
    }

    private void setMetadata(PhotoMetadata photoMetadata) {
        this.photoMetadata = photoMetadata;
    }

    public Integer getUserRatingTotal() {
        return userRatingTotal;
    }

    public void setUserRatingTotal(Integer userRatingTotal) {
        this.userRatingTotal = userRatingTotal;
    }

    public int getUserDistance() {
        return userDistance;
    }

    public void setUserDistance(int userDistance) {
        this.userDistance = userDistance;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public float getMarkedColor() {
        return markedColor;
    }

    public void setMarkedColor(float markedColor) {
        this.markedColor = markedColor;
    }

    public byte[] getBytesImage() {
        return bytesImage;
    }

    public void setBytesImage(byte[] bytesImage) {
        this.bytesImage = bytesImage;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
