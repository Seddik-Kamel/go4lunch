package com.example.go4lunch.infrastructure.entity;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.go4lunch.model.RestaurantModel;

import java.io.ByteArrayOutputStream;

@Entity(tableName = "restaurant_table")
public class RestaurantEntity {

    @PrimaryKey
    @NonNull
    private String placeId;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    private String name;
    private String address;

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

    private String phoneNumber;
    private String webSitUri;
    private float rating;

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

        return restaurantEntity;
    }


    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(@NonNull String placeId) {
        this.placeId = placeId;
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


    private static byte[] convertBitMapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);

        return stream.toByteArray();
    }
}

