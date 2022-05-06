package com.example.go4lunch.infrastructure.entity;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.go4lunch.model.RestaurantModel;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "restaurant_table")
public class RestaurantEntity {

    @PrimaryKey
    @NonNull
    private String placeId;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;
    private String name;
    private String address;
    private String phoneNumber;
    private String webSitUri;
    private float rating;
    public double latitude;
    public double longitude;
    private String isOpen;
    private int userDistance;
    private Integer userRatingTotal;
    private boolean isLiked;
    private float markedColor;


    public RestaurantEntity() {

    }

    public static RestaurantEntity updateData(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setName(restaurantModel.getName());
        restaurantEntity.setAddress(restaurantModel.getAddress());
        restaurantEntity.setPlaceId(restaurantModel.getPlaceId());
        restaurantEntity.setPhoneNumber(restaurantModel.getPhoneNumber());
        restaurantEntity.setWebSitUri(restaurantModel.getWebSitUri());
        restaurantEntity.setRating(restaurantModel.getRating());
        restaurantEntity.setImage(convertBitMapToByteArray(restaurantModel.getBitmap()));
        restaurantEntity.setLongitude(restaurantModel.getLatLng().longitude);
        restaurantEntity.setLatitude(restaurantModel.getLatLng().latitude);
        restaurantEntity.setIsOpen(restaurantModel.isOpen());
        restaurantEntity.setUserDistance(restaurantModel.getUserDistance());
        restaurantEntity.setUserRatingTotal(restaurantModel.getUserRatingTotal());
        restaurantEntity.setLiked(restaurantModel.isLiked());

        return restaurantEntity;
    }

    public static ArrayList<RestaurantModel> updateRestaurantModel(List<RestaurantEntity> restaurantEntities) {
        ArrayList<RestaurantModel> restaurantModels = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            RestaurantModel restaurantModel = new RestaurantModel();
            LatLng latLng = new LatLng(restaurantEntity.getLatitude(), restaurantEntity.getLongitude());
            restaurantModel.setLongitude(restaurantEntity.getLongitude());
            restaurantModel.setLatitude(restaurantEntity.getLatitude());
            restaurantModel.setLatLng(latLng);
            restaurantModel.setName(restaurantEntity.getName());
            restaurantModel.setName(restaurantEntity.getName());
            restaurantModel.setAddress(restaurantEntity.getAddress());
            restaurantModel.setPlaceId(restaurantEntity.getPlaceId());
            restaurantModel.setPhoneNumber(restaurantEntity.getPhoneNumber());
            restaurantModel.setRating(restaurantEntity.getRating());
            restaurantModel.setIsOpen(restaurantEntity.getIsOpen());
            restaurantModel.setUserDistance(restaurantEntity.getUserDistance());
            restaurantModel.setUserRatingTotal(restaurantEntity.getUserRatingTotal());
            restaurantModel.setWebSitUrl(restaurantEntity.getWebSitUri());
            restaurantModel.setMarkedColor(restaurantEntity.markedColor);
            restaurantModel.setLiked(restaurantEntity.isLiked());
            restaurantModel.setBytesImage(restaurantEntity.getImage());

            restaurantModels.add(restaurantModel);
        }

        return restaurantModels;
    }


    public static ArrayList<RestaurantEntity> updateRestaurantEntity(List<RestaurantModel> restaurantModels) {
        ArrayList<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for (RestaurantModel restaurantModel : restaurantModels) {
            RestaurantEntity restaurantEntity = new RestaurantEntity();
            restaurantEntity.setName(restaurantEntity.getName());
            restaurantEntity.setLongitude(restaurantModel.getLongitude());
            restaurantEntity.setLatitude(restaurantModel.getLatitude());
            restaurantEntity.setName(restaurantModel.getName());
            restaurantEntity.setAddress(restaurantModel.getAddress());
            restaurantEntity.setPlaceId(restaurantModel.getPlaceId());
            restaurantEntity.setPhoneNumber(restaurantModel.getPhoneNumber());
            restaurantEntity.setRating(restaurantModel.getRating());
            restaurantEntity.setIsOpen(restaurantModel.isOpen());
            restaurantEntity.setUserDistance(restaurantModel.getUserDistance());
            restaurantEntity.setUserRatingTotal(restaurantModel.getUserRatingTotal());
            restaurantEntity.setWebSitUri(restaurantModel.getWebSitUri());
            restaurantEntity.setMarkedColor(restaurantModel.getMarkedColor());
            restaurantEntity.setLiked(restaurantModel.isLiked());
            restaurantEntity.setImage(restaurantModel.getBytesImage());

            restaurantEntities.add(restaurantEntity);
        }

        return restaurantEntities;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(@NonNull String placeId) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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

    public void setWebSitUri(String webSitUri) {
        this.webSitUri = webSitUri;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private static byte[] convertBitMapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        return stream.toByteArray();
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public int getUserDistance() {
        return userDistance;
    }

    public void setUserDistance(int userDistance) {
        this.userDistance = userDistance;
    }

    public Integer getUserRatingTotal() {
        return userRatingTotal;
    }

    public void setUserRatingTotal(Integer userRatingTotal) {
        this.userRatingTotal = userRatingTotal;
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
}

