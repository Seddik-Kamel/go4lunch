package com.example.go4lunch.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityDetailsRestaurantsBinding;
import com.example.go4lunch.infrastructure.entity.PlaceEntity;
import com.example.go4lunch.infrastructure.repository.FirebaseRepository;
import com.example.go4lunch.model.WorkmateModel;
import com.example.go4lunch.state.FavoriteRestaurantState;
import com.example.go4lunch.state.RestaurantLikedState;
import com.example.go4lunch.state.WorkMatesUpdateState;
import com.example.go4lunch.ui.recyclerView.adapters.WorkmateLikedRestaurantAdapter;
import com.example.go4lunch.ui.viewmodel.RestaurantDetailViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;

import java.util.ArrayList;


public class RestaurantDetailActivity extends BaseActivity<ActivityDetailsRestaurantsBinding> {
    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;

    private String placeId;
    private String phoneNumber;
    private RestaurantDetailViewModel restaurantDetailViewModel;
    private boolean isLiked;


    public interface AlertDialogInterface {
        void doSomething();
    }

    @Override
    ActivityDetailsRestaurantsBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_details_restaurants);
    }

    @Override
    Activity getActivity() {
        return RestaurantDetailActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeId = getIntent().getStringExtra("placeId");
       // placeId = "ChIJhbeFtlPIlkcR83m5lfVPwB0";
        initViewModel();
        initObservers();
        likePlace();
        disLikePlace();
    }

    private void initObservers() {
        restaurantDetailViewModel.listenWorkmateWhoLikePlace(placeId);
        restaurantDetailViewModel.state.observe(this, this::workmateRender);
        restaurantDetailViewModel.onLoadView();
        restaurantDetailViewModel.onLoadViewFavoritePlace();
        restaurantDetailViewModel.onLoadViewLikedPlace();
        restaurantDetailViewModel.getRestaurant(placeId).observe(this, this::placeRender);
        restaurantDetailViewModel.updateFavoriteRestaurantListener(placeId);
        restaurantDetailViewModel.favoriteRestaurantState.observe(this, this::favoritePlaceRender);
        restaurantDetailViewModel.likedRestaurantState.observe(this, this::likedRestaurantRender);
    }

    private void likedRestaurantRender(RestaurantLikedState restaurantLikedState) {
        if (placeId != null)
            isLiked = restaurantDetailViewModel.restaurantHasLiked(restaurantLikedState.getRestaurantModelArrayList(), placeId);
    }


    private void favoritePlaceRender(@NonNull FavoriteRestaurantState favoriteRestaurantState) {
        if (restaurantDetailViewModel.hasAFavoriteRestaurant(favoriteRestaurantState.getFavoriteRestaurantModel(), placeId)) {
            binding.actionNoStar.setVisibility(View.GONE);
            binding.actionStar.setVisibility(View.VISIBLE);

            binding.actionStar.setOnClickListener(click -> {
                restaurantDetailViewModel.deleteFavoriteRestaurant(placeId);
                binding.actionNoStar.setVisibility(View.VISIBLE);
                binding.actionStar.setVisibility(View.INVISIBLE);
            });
        }
    }

    private void placeRender(PlaceEntity placeEntity) {// from local database.
        if (placeEntity != null) {
            binding.restaurantName.setText(placeEntity.getName());
            binding.restaurantAddress.setText(placeEntity.getAddress());
            binding.restaurantRatingBar.setRating(placeEntity.getRating());
            loadImageInView(placeEntity);

            if (hasNumberPhone(placeEntity.getPhoneNumber())) {
                binding.actionCall.setVisibility(View.VISIBLE);
                binding.actionCallDisable.setVisibility(View.INVISIBLE);
                binding.actionCall.setClickable(true);
                phoneNumber = placeEntity.getPhoneNumber();
                binding.actionCall.setOnClickListener(v -> askPermissionAndCall(phoneNumber));
            }

            if (hasALink(placeEntity.getWebSitUri())) {
                binding.actionWeb.setVisibility(View.VISIBLE);
                binding.actionNoWeb.setVisibility(View.GONE);
                binding.actionWeb.setOnClickListener(action -> launchPageWeb(placeEntity.getWebSitUri()));
            }

            binding.actionNoStar.setOnClickListener(click -> {
                restaurantDetailViewModel.saveFavoriteRestaurant(placeEntity);
                restaurantDetailViewModel.updateFavoriteRestaurantListener(placeId);
            });
        }
    }

    private void workmateRender(@NonNull WorkMatesUpdateState workMatesUpdateState) {
        initRecyclerView(workMatesUpdateState.getWorkmateModelArrayList());
        boolean hasLikedThisRestaurant = restaurantDetailViewModel.hasLikedThisRestaurant(workMatesUpdateState.getWorkmateModelArrayList());
        manageLikeFab(hasLikedThisRestaurant);
    }

    private void disLikePlace() {
        binding.floatingActionButtonDislike.setOnClickListener(click -> {
            String title = getResources().getString(R.string.alert_dialog_title);
            String message = getResources().getString(R.string.alert_dialog_restaurant_detail_dislike_message);
            showAlertDialog(() -> {
                displayLikeButton();
                restaurantDetailViewModel.deleteUserWhoLikedRestaurant(FirebaseRepository.getCurrentUserUID());
                restaurantDetailViewModel.deleteRestaurantLiked(placeId);

            }, title, message);
        });
    }

    private void likePlace() {
        binding.floatingActionButtonLike.setOnClickListener(click -> {
            if (isLiked) {
                String title = getResources().getString(R.string.alert_dialog_title);
                String message = getResources().getString(R.string.alert_dialog_restaurant_detail_like_message);
                showAlertDialog(() -> {
                    displayDisLikeButton();
                    restaurantDetailViewModel.persistWorkmateLikedRestaurant(placeId);
                    restaurantDetailViewModel.saveRestaurantsLiked(placeId);
                }, title, message);
            } else {
                restaurantDetailViewModel.persistWorkmateLikedRestaurant(placeId);
                restaurantDetailViewModel.saveRestaurantsLiked(placeId);
                displayDisLikeButton();
            }
        });
    }

    private void initViewModel() {
        restaurantDetailViewModel = ViewModelFactory.getInstance(getApplicationContext(), getActivity().getApplication()).obtainViewModel(RestaurantDetailViewModel.class);
    }

    private void showAlertDialog(AlertDialogInterface alertDialogInterface, String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> alertDialogInterface.doSomething())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void manageLikeFab(boolean value) {
        if (value) {
            binding.floatingActionButtonDislike.setVisibility(View.VISIBLE);
            binding.floatingActionButtonLike.setVisibility(View.GONE);
        } else {
            binding.floatingActionButtonDislike.setVisibility(View.GONE);
            binding.floatingActionButtonLike.setVisibility(View.VISIBLE);
        }
    }

    private void displayDisLikeButton() {
        binding.floatingActionButtonDislike.setVisibility(View.VISIBLE);
        binding.floatingActionButtonLike.setVisibility(View.GONE);
    }

    private void displayLikeButton() {
        binding.floatingActionButtonLike.setVisibility(View.VISIBLE);
        binding.floatingActionButtonDislike.setVisibility(View.GONE);
    }

    private boolean hasALink(String webSitUri) {
        return webSitUri != null;
    }

    private boolean hasNumberPhone(String phoneNumber) {
        return phoneNumber != null;
    }

    private void loadImageInView(PlaceEntity placeEntity) {
        Glide.with(getApplicationContext())
                .load(placeEntity.getImage())
                .into(binding.imageView);
    }

    public void launchPageWeb(String link) {
        Intent launchWeb = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

        try {
            this.startActivity(launchWeb);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Your action failed.. " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void askPermissionAndCall(String phoneNumber) {
        // Check if we have Call permission
        int sendSmsPermission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE);

        if (sendSmsPermission != PackageManager.PERMISSION_GRANTED) {
            // If don't have permission so prompt the user.
            this.requestPermissions(
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSION_REQUEST_CODE_CALL_PHONE
            );
            return;
        }
        this.callNow(phoneNumber);
    }

    @SuppressLint("MissingPermission")
    private void callNow(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            this.startActivity(callIntent);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Your call failed... " + ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        if (requestCode == MY_PERMISSION_REQUEST_CODE_CALL_PHONE) {// Note: If request is cancelled, the result arrays are empty.
            // Permissions granted (CALL_PHONE).
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i("LOG_TAG", "Permission granted!");
                Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();
                this.callNow(phoneNumber);
            }
            // Cancelled or denied.
            else {
                Log.i("LOG_TAG", "Permission denied!");
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_CALL_PHONE) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initRecyclerView(ArrayList<WorkmateModel> workmateList) {
        WorkmateLikedRestaurantAdapter adapter = new WorkmateLikedRestaurantAdapter(workmateList);
        binding.workmatesRecyclerview.setAdapter(adapter);
        binding.workmatesRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.workmatesRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}
