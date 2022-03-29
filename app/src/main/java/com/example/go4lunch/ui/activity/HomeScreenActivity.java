package com.example.go4lunch.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityHomeScreenBinding;
import com.example.go4lunch.ui.fragments.ListViewRestaurantsFragment;
import com.example.go4lunch.ui.fragments.MapViewFragment;
import com.example.go4lunch.ui.fragments.WorkmatesListFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeScreenActivity extends PermissionBaseActivity<ActivityHomeScreenBinding>/* implements NavigationView.OnNavigationItemSelectedListener*/ {

    private Bundle bundle;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public HomeScreenActivity() {
    }

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

       /* configureToolBar();
        configureDrawerLayout();
        configureBottomView();
        configureNavigationView();*/
    }

   /* @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id) {
            case R.id.activity_main_drawer_news:
                break;
            case R.id.activity_main_drawer_profile:
                break;
            case R.id.activity_main_drawer_settings:
                break;
            default:
                break;
        }

        //binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);
        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
*/
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

    public void displayFragment(Bundle savedInstanceState, Fragment fragment) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activity_main_frame_layout, fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void displayFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.activity_main_frame_layout, fragment, null)
                .addToBackStack(null)
                .commit();
    }

    public void startRestaurantDetailActivity(String placeId) {
        Intent intent = new Intent(this, RestaurantDetailActivity.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
    }

    public void setActionBarTitle(int title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

    // Call for disable backPressed without super.onBackPressed()
    @Override
    public void onBackPressed() {
    }

  /*  private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.activityMainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }
    private void configureToolBar() {
       // setSupportActionBar(binding.toolbar);
    }

    private void configureNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener(this);
    }*/

   /* private void configureToolBar() {
        this.toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }*/
}