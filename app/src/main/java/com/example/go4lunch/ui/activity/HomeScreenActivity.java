package com.example.go4lunch.ui.activity;


import android.app.Activity;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;

import com.example.go4lunch.databinding.ActivityHomeScreenBinding;
import com.example.go4lunch.ui.fragments.ListViewRestaurantsFragment;
import com.example.go4lunch.ui.fragments.MapViewFragment;
import com.example.go4lunch.ui.fragments.WorkmatesListFragment;

public class HomeScreenActivity extends PermissionBaseActivity<ActivityHomeScreenBinding> {

    private Bundle bundle;

    @Override
    ActivityHomeScreenBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
    }

    @Override
    Activity getActivity() {
        return HomeScreenActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bundle = savedInstanceState;
        displayFragment(savedInstanceState, MapViewFragment.newInstance(isLocationPermissionGranted()));
        configureBottomView();
    }

    private void configureBottomView() {
        binding.activityHomeBottomNavigation.setOnItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    private boolean updateMainFragment(int itemId) {
        if (itemId == R.id.action_map) {
            displayFragment(bundle, MapViewFragment.newInstance(isLocationPermissionGranted()));
        } else if (itemId == R.id.action_list) {
            displayFragment(bundle, new ListViewRestaurantsFragment());
        } else if (itemId == R.id.action_workmates) {
            displayFragment(bundle, new WorkmatesListFragment());
        }

        return true;
    }

    private void displayFragment(Bundle savedInstanceState, Fragment fragment) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activity_main_frame_layout, fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void setActionBarTitle(int title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    // Call for disable backPressed without super.onBackPressed()
    @Override
    public void onBackPressed() {
    }
}