package com.example.go4lunch.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.infrastructure.entity.PlaceEntity;
import com.example.go4lunch.infrastructure.repository.FirebaseRepository;
import com.example.go4lunch.infrastructure.repository.PlaceRepository;
import com.example.go4lunch.model.FavoriteRestaurantModel;
import com.example.go4lunch.state.FavoriteRestaurantState;
import com.example.go4lunch.state.RestaurantLikedState;
import com.example.go4lunch.state.WorkMatesUpdateState;
import com.example.go4lunch.usecase.FavoriteRestaurantUseCase;
import com.example.go4lunch.usecase.RestaurantLikedUseCase;
import com.example.go4lunch.usecase.WorkMatesUseCase;

import java.util.ArrayList;

public class RestaurantDetailViewModel extends ViewModel {

    private final FavoriteRestaurantUseCase favoriteRestaurantUseCase;
    private final PlaceRepository placeRepository;
    private final WorkMatesUseCase workMatesUseCase;
    private final RestaurantLikedUseCase restaurantLikedUseCase;

    private final MutableLiveData<WorkMatesUpdateState> _state = new MutableLiveData<>();
    public final LiveData<WorkMatesUpdateState> state = _state;
    private final MutableLiveData<FavoriteRestaurantState> _favoriteRestaurantState = new MutableLiveData<>();
    public final LiveData<FavoriteRestaurantState> favoriteRestaurantState = _favoriteRestaurantState;
    private final MutableLiveData<RestaurantLikedState> _likedRestaurantState = new MutableLiveData<>();
    public final LiveData<RestaurantLikedState> likedRestaurantState = _likedRestaurantState;

    public RestaurantDetailViewModel(
            FavoriteRestaurantUseCase favoriteRestaurantUseCase,
            WorkMatesUseCase workMatesUseCase,
            RestaurantLikedUseCase restaurantLikedUseCase,
            PlaceRepository placeRepository) {

        this.favoriteRestaurantUseCase = favoriteRestaurantUseCase;
        this.workMatesUseCase = workMatesUseCase;
        this.restaurantLikedUseCase = restaurantLikedUseCase;
        this.placeRepository = placeRepository;
    }

    public void onLoadView() {
        workMatesUseCase.observeForever(workMatesUpdateState -> _state.setValue(new WorkMatesUpdateState(workMatesUpdateState.getWorkmateModelArrayList())));
    }

    public void onLoadViewFavoritePlace() {
        favoriteRestaurantUseCase.observeForever(favoriteRestaurantState -> _favoriteRestaurantState.setValue(new FavoriteRestaurantState(favoriteRestaurantState.getFavoriteRestaurantModel())));
    }

    public void onLoadViewLikedPlace() {
        restaurantLikedUseCase.observeForever(restaurantLikedState -> _likedRestaurantState.setValue(new RestaurantLikedState(restaurantLikedState.getRestaurantModelArrayList())));
    }

    public LiveData<PlaceEntity> getRestaurant(String placeId) {
        return placeRepository.getRestaurant(placeId);
    }

    public void saveFavoriteRestaurant(PlaceEntity placeEntity) {
        favoriteRestaurantUseCase.saveFavoriteRestaurant(placeEntity);
    }

    public void deleteFavoriteRestaurant(String placeId) {
        favoriteRestaurantUseCase.deleteFavoriteRestaurant(placeId);
    }

    public void updateFavoriteRestaurantListener(String placeId) {
        favoriteRestaurantUseCase.updateFavoriteRestaurantListener(placeId);
    }

    public void listenWorkmateWhoLikePlace(String placeId) {
        workMatesUseCase.listenWorkmateWhoLikePlace(placeId);
    }

    public void deleteUserWhoLikedRestaurant(String userUID) {
        workMatesUseCase.deleteUserWhoLikedRestaurant(userUID);
    }

    public void saveRestaurantsLiked(String placeId) {
        this.restaurantLikedUseCase.saveRestaurantsLiked(placeId);
    }

    public void deleteRestaurantLiked(String placeId) {
        restaurantLikedUseCase.deleteRestaurantLiked(placeId);
    }

    public boolean hasAFavoriteRestaurant(ArrayList<FavoriteRestaurantModel> favoriteRestaurantModelArrayList, String placeId) {
        boolean isFavoriteRestaurant = false;

        for (FavoriteRestaurantModel favoriteRestaurantModel : favoriteRestaurantModelArrayList) {
            if (favoriteRestaurantModel.getPlaceId().equals(placeId)) {
                isFavoriteRestaurant = true;
                break;
            }
        }

        return isFavoriteRestaurant;
    }

    public boolean placeHasLiked(ArrayList<String> listDocumentId, String placeId) {
        String userId = FirebaseRepository.getCurrentUserUID();
        String uid = placeId + userId;
        boolean placeHasLikedByUser = false;

        for (String documentId : listDocumentId) {
            if (documentId.equals(uid)) {
                placeHasLikedByUser = true;
                break;
            }
        }

        return placeHasLikedByUser;
    }

    public boolean userHasLikedAnotherPlace(ArrayList<String> listDocumentId) {
        String currentUserId = FirebaseRepository.getCurrentUserUID();
        boolean userHasLikedPlace = false;

        if (currentUserId != null) {
            for (String documentId : listDocumentId) {
                int beginIndex = documentId.length() - currentUserId.length();
                String userId = documentId.substring(beginIndex);
                if (currentUserId.equals(userId)) {
                    userHasLikedPlace = true;
                    break;
                }
            }
        }

        return userHasLikedPlace;
    }

    public String getDocumentIdOfPlaceLiked(ArrayList<String> listDocumentId) {
        String currentUserId = FirebaseRepository.getCurrentUserUID();
        String placeIdOfPlaceLiked = "";
        if (currentUserId != null) {
            for (String documentId : listDocumentId) {
                int beginIndex = documentId.length() - currentUserId.length();

                String userId = documentId.substring(beginIndex);
                if (currentUserId.equals(userId)) {
                    placeIdOfPlaceLiked = documentId.substring(0, beginIndex);
                    break;
                }
            }
        }
        return placeIdOfPlaceLiked;
    }
}
