package com.example.go4lunch.model;

import androidx.annotation.Nullable;

public class WorkmateModel {

    private String userUid;
    private String userName;
    private String placeIdLiked;
    @Nullable
    private String urlPicture;


    /**
     * Constructor needed for Firebase to convert to object (toObject)
     */
    public WorkmateModel() {

    }

    public WorkmateModel(String uid, String userName, @Nullable String urlPicture) {
        this.userUid = uid;
        this.userName = userName;
        this.urlPicture = urlPicture;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlaceIdLiked() {
        return placeIdLiked;
    }

    public void setPlaceIdLiked(String placeIdLiked) {
        this.placeIdLiked = placeIdLiked;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }
}
