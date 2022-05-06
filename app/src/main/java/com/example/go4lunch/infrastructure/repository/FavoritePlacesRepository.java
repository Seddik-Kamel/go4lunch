package com.example.go4lunch.infrastructure.repository;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.entity.PlaceEntity;
import com.example.go4lunch.model.FavoriteRestaurantModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FavoritePlacesRepository extends LiveData<ArrayList<FavoriteRestaurantModel>> {

    private static final String FAVORITE_RESTAURANT = "favorite_restaurant";
    private static FavoritePlacesRepository instance;

    private CollectionReference getFavoriteRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection(FAVORITE_RESTAURANT);
    }

    public static FavoritePlacesRepository getInstance() {
        FavoritePlacesRepository result = instance;
        if (result != null)
            return instance;
        synchronized (FavoritePlacesRepository.class) {
            if (instance == null) {
                instance = new FavoritePlacesRepository();
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

    public void saveFavoriteRestaurantByWorkmate(PlaceEntity placeEntity) {
        FavoriteRestaurantModel favoriteRestaurantModel = new FavoriteRestaurantModel(placeEntity.getName(), placeEntity.getPlaceId(), getCurrentUserUID());
        String documentId = getCurrentUserUID() + "_" + placeEntity.getPlaceId();
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
