package com.example.go4lunch.infrastructure.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.go4lunch.model.WorkmateModel;

import java.util.Date;

@Entity(tableName = "workmate_liked_restaurant")
public class WorkmateLikedRestaurantEntity {


    @PrimaryKey
    @NonNull
    private String placeId;
    @NonNull
    private String userId;
    private Date date;
    private String urlPicture;
    private String userName;


    public WorkmateLikedRestaurantEntity(@NonNull String placeId, @NonNull String userId, Date date, String urlPicture, String userName) {
        this.placeId = placeId;
        this.userId = userId;
        this.date = date;
        this.urlPicture = urlPicture;
        this.userName = userName;
    }

    public WorkmateLikedRestaurantEntity() {

    }

    public static WorkmateLikedRestaurantEntity updateData(WorkmateModel workmateModel, String placeId, Date date) {
        WorkmateLikedRestaurantEntity workmateLikedRestaurantEntity = new WorkmateLikedRestaurantEntity();
        workmateLikedRestaurantEntity.setUserId(workmateModel.getUserUid());
        workmateLikedRestaurantEntity.setUserName(workmateModel.getUserName());
        workmateLikedRestaurantEntity.setUrlPicture(workmateModel.getUrlPicture());
        workmateLikedRestaurantEntity.setPlaceId(placeId);
        workmateLikedRestaurantEntity.setDate(date);

        return workmateLikedRestaurantEntity;
    }


    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(@NonNull String placeId) {
        this.placeId = placeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
