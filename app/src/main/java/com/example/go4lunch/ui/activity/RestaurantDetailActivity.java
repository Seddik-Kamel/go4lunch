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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;

import com.example.go4lunch.databinding.ActivityDetailsRestaurantsBinding;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.model.WorkmateModel;
import com.example.go4lunch.ui.recyclerView.adapters.WorkmateLikedRestaurantAdapter;
import com.example.go4lunch.ui.viewmodel.RestaurantViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.ui.viewmodel.WorkMateViewModel;
import com.example.go4lunch.usecase.WorkMatesUpdateState;
import com.example.go4lunch.utils.Preferences;

import java.util.ArrayList;

public class RestaurantDetailActivity extends BaseActivity<ActivityDetailsRestaurantsBinding> {
    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;
    public static final int NO_RESTAURANT_LIKED = 0;
    private WorkMateViewModel workMateViewModel;
    private String placeId;
    private Preferences preferences;
    private WorkmateLikedRestaurantAdapter adapter;

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
        preferences = new Preferences(this);
        initViewModel();

        binding.actionStar.setOnClickListener(v -> {

        });

        binding.floatingActionButtonLike.setOnClickListener(click -> {
            if (preferences.getBoolean(Preferences.HAS_LIKED_RESTAURANT)) {
                String title = getResources().getString(R.string.alert_dialog_title);
                String message = getResources().getString(R.string.alert_dialog_restaurant_detail_like_message);
                showAlertDialog(() -> {
                    displayDisLikeButton();
                    workMateViewModel.persistWorkmateLikedRestaurant(placeId);
                }, title, message);
            } else {
                workMateViewModel.persistWorkmateLikedRestaurant(placeId);
                preferences.setBoolean(Preferences.HAS_LIKED_RESTAURANT, true);
                displayDisLikeButton();
            }
        });

        binding.floatingActionButtonDislike.setOnClickListener(click -> {
            String title = getResources().getString(R.string.alert_dialog_title);
            String message = getResources().getString(R.string.alert_dialog_restaurant_detail_dislike_message);
            showAlertDialog(() -> {
                displayLikeButton();
                preferences.setBoolean(Preferences.HAS_LIKED_RESTAURANT, false);
                workMateViewModel.deleteUserWhoLikedRestaurant(workMateViewModel.getCurrentUser().getUid());
            }, title, message);
        });

        initRecyclerView();

        workMateViewModel.state.observe(this, this::workmateRender);

    }

    private void initViewModel() {
        RestaurantViewModel restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        workMateViewModel = ViewModelFactory.getInstance(getApplicationContext(), getApplication()).obtainViewModel(WorkMateViewModel.class);
        workMateViewModel.onLoadView();
        restaurantViewModel.getRestaurant(placeId).observe(this, this::render);
        workMateViewModel.addListener(placeId);
    }

    private void render(RestaurantEntity restaurantEntity) {
        if (restaurantEntity != null) {
            binding.restaurantName.setText(restaurantEntity.getName());
            binding.restaurantAddress.setText(restaurantEntity.getPlaceId());
            binding.restaurantRatingBar.setRating(restaurantEntity.getRating());
            loadImageInView(restaurantEntity);

            if (hasNumberPhone(restaurantEntity.getPhoneNumber())) {
                binding.actionCall.setVisibility(View.VISIBLE);
                binding.actionCallDisable.setVisibility(View.INVISIBLE);
                binding.actionCall.setClickable(true);
                binding.actionCall.setOnClickListener(v -> askPermissionAndCall(restaurantEntity.getPhoneNumber()));
            }

            if (hasALink(restaurantEntity.getWebSitUri())) {
                binding.actionWeb.setOnClickListener(action -> launchPageWeb(restaurantEntity.getWebSitUri()));
            }
        }
    }

    private void workmateRender(WorkMatesUpdateState workMatesUpdateState) {
        workMateViewModel.setWorkmateList(workMatesUpdateState.getWorkmateModelArrayList());
        final ArrayList<WorkmateModel> workmateList = workMateViewModel.getWorkmateList();
        boolean hasLikedThisRestaurant = workMateViewModel.hasLikedThisRestaurant(workmateList);

        initRecyclerView();
        manageFab(hasLikedThisRestaurant);
    }

    private void showAlertDialog(AlertDialogInterface alertDialogInterface, String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> alertDialogInterface.doSomething())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void manageFab(boolean value) {
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

    private void loadImageInView(RestaurantEntity restaurantEntity) {
        Glide.with(getApplicationContext())
                .load(restaurantEntity.getImage())
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

        // With Android Level >= 23, you have to ask the user
        // for permission to Call.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

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

                // this.callNow();
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

    private void initRecyclerView() {
        adapter = new WorkmateLikedRestaurantAdapter(workMateViewModel.getWorkmateList());
        binding.workmatesRecyclerview.setAdapter(adapter);
        binding.workmatesRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.workmatesRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}
