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

public class HomeScreenActivity extends BaseActivity<ActivityHomeScreenBinding> {

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
        configureAndShowFragment(savedInstanceState, MapViewFragment.class);
        configureBottomView();
    }

    private void configureBottomView() {
        binding.activityHomeBottomNavigation.setOnItemSelectedListener(item -> updateMainFragment(item.getItemId()));
    }

    private boolean updateMainFragment(int itemId) {
        if (itemId == R.id.action_map) {
            configureAndShowFragment(bundle, MapViewFragment.class);
        } else if (itemId == R.id.action_list) {
            configureAndShowFragment(bundle, ListViewRestaurantsFragment.class);
        } else if (itemId == R.id.action_workmates) {
            configureAndShowFragment(bundle, WorkmatesListFragment.class);
        }

        return true;
    }

    private void configureAndShowFragment(Bundle savedInstanceState, Class<? extends Fragment> fragment) {
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