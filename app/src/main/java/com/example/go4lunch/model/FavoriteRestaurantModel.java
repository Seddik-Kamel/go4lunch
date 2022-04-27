package com.example.go4lunch.model;

public class FavoriteRestaurantModel {

    private String placeName;
    private String placeId;
    private String userId;


    /**
     * Constructor needed for Firebase to convert to object (toObject)
     */
    public FavoriteRestaurantModel() {

    }

    public FavoriteRestaurantModel(String placeName, String placeId, String userId) {
        this.placeName = placeName;
        this.placeId = placeId;
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
