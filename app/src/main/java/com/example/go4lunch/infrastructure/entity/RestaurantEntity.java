package com.example.go4lunch.infrastructure.entity;

import android.graphics.Bitmap;
import android.location.Location;

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
        return restaurantEntity;
    }

    public static ArrayList<RestaurantModel> updateRestaurantModel(List<RestaurantEntity> restaurantEntities) {
        ArrayList<RestaurantModel> restaurantModels = new ArrayList<>();
        for (RestaurantEntity restaurantEntity : restaurantEntities) {
            RestaurantModel restaurantModel = new RestaurantModel();
            LatLng latLng = new LatLng(restaurantEntity.getLatitude(), restaurantEntity.getLongitude());
            restaurantModel.setName(restaurantEntity.getName());
            restaurantModel.setLatLng(latLng);

            restaurantModels.add(restaurantModel);
        }

        return restaurantModels;
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
}

