package com.example.go4lunch.infrastructure.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.model.FavoriteRestaurantModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FavoriteRestaurantRepository extends LiveData<ArrayList<FavoriteRestaurantModel>> {

    private static final String FAVORITE_RESTAURANT = "favorite_restaurant";
    private static FavoriteRestaurantRepository instance;

    private CollectionReference getFavoriteRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection(FAVORITE_RESTAURANT);
    }

    public static FavoriteRestaurantRepository getInstance() {
        FavoriteRestaurantRepository result = instance;
        if (result != null)
            return instance;
        synchronized (FavoriteRestaurantRepository.class) {
            if (instance == null) {
                instance = new FavoriteRestaurantRepository();
            }
        }
        return instance;
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseRepository.getAuth().getCurrentUser();
    }

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public void saveFavoriteRestaurantByWorkmate(RestaurantEntity restaurantEntity) {
        FavoriteRestaurantModel favoriteRestaurantModel = new FavoriteRestaurantModel(restaurantEntity.getName(), restaurantEntity.getPlaceId(), getCurrentUserUID());
        String documentId = getCurrentUserUID() + "_" + restaurantEntity.getPlaceId();
        getFavoriteRestaurantCollection()
                .document(documentId)
                .set(favoriteRestaurantModel)
                .addOnSuccessListener(event -> Log.d("add favorite restaurant", "ok"))
                .addOnFailureListener(event -> Log.w("add favorite restaurant ", event.getMessage()));
    }

    public void deleteFavoriteRestaurant(String placeId) {
        String documentId = getCurrentUserUID() + "_" + placeId;
        getFavoriteRestaurantCollection()
                .document(documentId)
                .delete();
    }

    public void updateFavoriteRestaurantListener(String placeId) {
        getFavoriteRestaurantCollection()
                .whereEqualTo("userId", getCurrentUserUID())
                .get()
                .addOnSuccessListener(task -> {
                    ArrayList<FavoriteRestaurantModel> favoriteRestaurantModelArrayList = new ArrayList<>();
                    String documentId = getCurrentUserUID() + "_" + placeId;
                    for (DocumentSnapshot document : task.getDocuments()) {
                        FavoriteRestaurantModel favoriteRestaurantModel = document.toObject(FavoriteRestaurantModel.class);
                        favoriteRestaurantModelArrayList.add(favoriteRestaurantModel);
                    }

                    setValue(favoriteRestaurantModelArrayList);
                });
    }
}
