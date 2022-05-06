package com.example.go4lunch.infrastructure.entity;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.go4lunch.model.PlaceModel;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "place_table")
public class PlaceEntity {

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


    public PlaceEntity() {

    }

    public static PlaceEntity updateData(PlaceModel placeModel) {
        PlaceEntity placeEntity = new PlaceEntity();
        placeEntity.setName(placeModel.getName());
        placeEntity.setAddress(placeModel.getAddress());
        placeEntity.setPlaceId(placeModel.getPlaceId());
        placeEntity.setPhoneNumber(placeModel.getPhoneNumber());
        placeEntity.setWebSitUri(placeModel.getWebSitUri());
        placeEntity.setRating(placeModel.getRating());
        placeEntity.setImage(convertBitMapToByteArray(placeModel.getBitmap()));
        placeEntity.setLongitude(placeModel.getLatLng().longitude);
        placeEntity.setLatitude(placeModel.getLatLng().latitude);
        placeEntity.setIsOpen(placeModel.isOpen());
        placeEntity.setUserDistance(placeModel.getUserDistance());
        placeEntity.setUserRatingTotal(placeModel.getUserRatingTotal());
        placeEntity.setLiked(placeModel.isLiked());

        return placeEntity;
    }

    public static ArrayList<PlaceModel> updateRestaurantModel(List<PlaceEntity> restaurantEntities) {
        ArrayList<PlaceModel> placeModels = new ArrayList<>();
        for (PlaceEntity placeEntity : restaurantEntities) {
            PlaceModel placeModel = new PlaceModel();
            LatLng latLng = new LatLng(placeEntity.getLatitude(), placeEntity.getLongitude());
            placeModel.setLongitude(placeEntity.getLongitude());
            placeModel.setLatitude(placeEntity.getLatitude());
            placeModel.setLatLng(latLng);
            placeModel.setName(placeEntity.getName());
            placeModel.setName(placeEntity.getName());
            placeModel.setAddress(placeEntity.getAddress());
            placeModel.setPlaceId(placeEntity.getPlaceId());
            placeModel.setPhoneNumber(placeEntity.getPhoneNumber());
            placeModel.setRating(placeEntity.getRating());
            placeModel.setIsOpen(placeEntity.getIsOpen());
            placeModel.setUserDistance(placeEntity.getUserDistance());
            placeModel.setUserRatingTotal(placeEntity.getUserRatingTotal());
            placeModel.setWebSitUrl(placeEntity.getWebSitUri());
            placeModel.setMarkedColor(placeEntity.markedColor);
            placeModel.setLiked(placeEntity.isLiked());
            placeModel.setBytesImage(placeEntity.getImage());

            placeModels.add(placeModel);
        }

        return placeModels;
    }


    public static ArrayList<PlaceEntity> updateRestaurantEntity(List<PlaceModel> placeModels) {
        ArrayList<PlaceEntity> restaurantEntities = new ArrayList<>();
        for (PlaceModel placeModel : placeModels) {
            PlaceEntity placeEntity = new PlaceEntity();
            placeEntity.setName(placeEntity.getName());
            placeEntity.setLongitude(placeModel.getLongitude());
            placeEntity.setLatitude(placeModel.getLatitude());
            placeEntity.setName(placeModel.getName());
            placeEntity.setAddress(placeModel.getAddress());
            placeEntity.setPlaceId(placeModel.getPlaceId());
            placeEntity.setPhoneNumber(placeModel.getPhoneNumber());
            placeEntity.setRating(placeModel.getRating());
            placeEntity.setIsOpen(placeModel.isOpen());
            placeEntity.setUserDistance(placeModel.getUserDistance());
            placeEntity.setUserRatingTotal(placeModel.getUserRatingTotal());
            placeEntity.setWebSitUri(placeModel.getWebSitUri());
            placeEntity.setMarkedColor(placeModel.getMarkedColor());
            placeEntity.setLiked(placeModel.isLiked());
            placeEntity.setImage(placeModel.getBytesImage());

            restaurantEntities.add(placeEntity);
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

