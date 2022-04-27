package com.example.go4lunch.ui.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.infrastructure.repository.FirebaseRepository;
import com.example.go4lunch.infrastructure.repository.RestaurantRepository;
import com.example.go4lunch.model.FavoriteRestaurantModel;
import com.example.go4lunch.model.WorkmateModel;
import com.example.go4lunch.state.FavoriteRestaurantState;
import com.example.go4lunch.state.RestaurantLikedState;
import com.example.go4lunch.state.WorkMatesUpdateState;
import com.example.go4lunch.usecase.FavoriteRestaurantUseCase;
import com.example.go4lunch.usecase.RestaurantLikedUseCase;
import com.example.go4lunch.usecase.WorkMatesUseCase;

import java.util.ArrayList;

public class RestaurantDetailViewModel extends ViewModel {

    private final FavoriteRestaurantUseCase favoriteRestaurantUseCase;
    private final RestaurantRepository restaurantRepository;
    private final WorkMatesUseCase workMatesUseCase;
    private final RestaurantLikedUseCase restaurantLikedUseCase;

    private final MutableLiveData<WorkMatesUpdateState> _state = new MutableLiveData<>();
    public final LiveData<WorkMatesUpdateState> state = _state;
    private final MutableLiveData<FavoriteRestaurantState> _favoriteRestaurantState = new MutableLiveData<>();
    public final LiveData<FavoriteRestaurantState> favoriteRestaurantState = _favoriteRestaurantState;
    private final MutableLiveData<RestaurantLikedState> _likedRestaurantState = new MutableLiveData<>();
    public final LiveData<RestaurantLikedState> likedRestaurantState = _likedRestaurantState;

    public RestaurantDetailViewModel(
            Application application,
            FavoriteRestaurantUseCase favoriteRestaurantUseCase,
            WorkMatesUseCase workMatesUseCase,
            RestaurantLikedUseCase restaurantLikedUseCase) {

        this.restaurantRepository = new RestaurantRepository(application);
        this.favoriteRestaurantUseCase = favoriteRestaurantUseCase;
        this.workMatesUseCase = workMatesUseCase;
        this.restaurantLikedUseCase = restaurantLikedUseCase;
    }

    public void onLoadView() {
        workMatesUseCase.observeForever(workMatesUpdateState -> _state.setValue(new WorkMatesUpdateState(workMatesUpdateState.getWorkmateModelArrayList())));
    }

    public void onLoadViewFavoritePlace() {
        favoriteRestaurantUseCase.observeForever(favoriteRestaurantState -> _favoriteRestaurantState.setValue(new FavoriteRestaurantState(favoriteRestaurantState.getFavoriteRestaurantModel())));
    }

    public void onLoadViewLikedPlace() {
        restaurantLikedUseCase.observeForever(restaurantLikedState -> {
            _likedRestaurantState.setValue(new RestaurantLikedState(restaurantLikedState.getRestaurantModelArrayList()));
        });
    }

    public LiveData<RestaurantEntity> getRestaurant(String placeId) {
        return restaurantRepository.getRestaurant(placeId);
    }

    public void saveFavoriteRestaurant(RestaurantEntity restaurantEntity) {
        favoriteRestaurantUseCase.saveFavoriteRestaurant(restaurantEntity);
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

    public void persistWorkmateLikedRestaurant(String placeId) {
        this.workMatesUseCase.persistWorkmateLikedRestaurant(placeId);
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

    public void getRestaurantsLiked(String placeId) {
        restaurantLikedUseCase.getRestaurantsLiked(placeId);
    }

    public boolean hasLikedThisRestaurant(ArrayList<WorkmateModel> workmateList) {
        String currentUserId = FirebaseRepository.getCurrentUserUID();
        boolean isLikedRestaurant = false;
        for (WorkmateModel workmateModel : workmateList) {
            isLikedRestaurant = workmateModel.getUserUid().equals(currentUserId);
        }
        return isLikedRestaurant;
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

    public boolean restaurantHasLiked(ArrayList<String> restaurantModelArrayList, String placeId) {
        String userId = FirebaseRepository.getCurrentUserUID();
        String uid = placeId + userId;
        boolean isLiked = false;

        for (String id : restaurantModelArrayList) {
            if (id.equals(uid)) {
                isLiked = true;
                break;
            }
        }

        return isLiked;
    }
}
