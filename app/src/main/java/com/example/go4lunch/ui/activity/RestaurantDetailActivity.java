package com.example.go4lunch.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.FragmentDetailsRestaurantsBinding;
import com.example.go4lunch.infrastructure.entity.RestaurantEntity;
import com.example.go4lunch.ui.viewmodel.RestaurantViewModel;

public class RestaurantDetailActivity extends BaseActivity<FragmentDetailsRestaurantsBinding> {
    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 555;

    @Override
    FragmentDetailsRestaurantsBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.fragment_details_restaurants);
    }

    @Override
    Activity getActivity() {
        return RestaurantDetailActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RestaurantViewModel restaurantViewModel = new ViewModelProvider(this).get(RestaurantViewModel.class);
        String placeId = getIntent().getStringExtra("placeId");
        restaurantViewModel.getRestaurant(placeId).observe(this, this::updateUi);
    }

    private void updateUi(RestaurantEntity restaurantEntity) {
        if (restaurantEntity != null) {
            binding.restaurantName.setText(restaurantEntity.getName());
            binding.restaurantAddress.setText(restaurantEntity.getAddress());
            binding.restaurantRatingBar.setRating(restaurantEntity.getRating());
            injectImageInView(restaurantEntity);

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

    private boolean hasALink(String webSitUri) {
        return webSitUri != null;
    }

    private boolean hasNumberPhone(String phoneNumber) {
        return phoneNumber != null;
    }

    private void injectImageInView(RestaurantEntity restaurantEntity) {
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
}
