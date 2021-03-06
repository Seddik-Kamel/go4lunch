package com.example.go4lunch.infrastructure.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.go4lunch.infrastructure.entity.PlaceEntity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PlaceLikedRepository extends LiveData<ArrayList<PlaceEntity>> {

    private static PlaceLikedRepository placeLikedRepository;
    private static final String RESTAURANT_LIKED = "restaurant_liked";
    private static final String PLACE_NAME_FIELD = "placeIdLiked";

    private CollectionReference getRestaurantLikedCollection() {
        return FirebaseFirestore.getInstance().collection(RESTAURANT_LIKED);
    }

    public PlaceLikedRepository() {
        addWorkmateSnapShotListener();
    }

    public static PlaceLikedRepository getInstance() {
        if (placeLikedRepository == null) {
            synchronized (PlaceLikedRepository.class) {
                if (placeLikedRepository == null) {
                    placeLikedRepository = new PlaceLikedRepository();
                }
            }
        }
        return placeLikedRepository;
    }

    private void addWorkmateSnapShotListener() {//TODO changer de nom.
        getRestaurantLikedCollection()
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (value != null) {
                        ArrayList<PlaceEntity> restaurantLiked = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                            restaurantLiked.add(queryDocumentSnapshot.toObject(PlaceEntity.class));
                        }

                        setValue(restaurantLiked);

                    } else {

                        Log.d("TAG", "Current data: null");
                    }
                });
    }

    public void saveRestaurantsLiked(PlaceEntity placeEntity) {
        String userId = FirebaseRepository.getCurrentUserUID();
        String placeId = placeEntity.getPlaceId();

        byte[] image = placeEntity.getImage();
        placeEntity.setImage(null);
        placeEntity.setCurrentUserID(userId);
        getRestaurantLikedCollection().document(placeId + userId).set(placeEntity)
                .addOnSuccessListener(event -> Log.d("add restaurant liked", "ok"))
                .addOnFailureListener(event -> Log.w("add restaurant liked", event.getMessage()));

        placeEntity.setImage(image);
    }

    public void deleteRestaurantLikedByWorkmate(String placeId) {
        String userId = FirebaseRepository.getCurrentUserUID();
        getRestaurantLikedCollection().document(placeId + userId).delete();
    }

    public void getRestaurantsLiked(String placeId) {
        getRestaurantLikedCollection()
                .whereEqualTo(PLACE_NAME_FIELD, placeId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ArrayList<PlaceEntity> restaurantLiked = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            restaurantLiked.add(document.toObject(PlaceEntity.class));
                        }

                        setValue(restaurantLiked);
                    }
                });
    }
}
