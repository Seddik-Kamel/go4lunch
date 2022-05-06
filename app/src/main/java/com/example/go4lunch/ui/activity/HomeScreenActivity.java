package com.example.go4lunch.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityHomeScreenBinding;
import com.example.go4lunch.infrastructure.repository.FirebaseRepository;
import com.example.go4lunch.model.PlaceModel;
import com.example.go4lunch.state.AutocompleteState;
import com.example.go4lunch.state.MainPageState;
import com.example.go4lunch.ui.fragments.ListViewRestaurantsFragment;
import com.example.go4lunch.ui.fragments.MapViewFragment;
import com.example.go4lunch.ui.fragments.WorkmatesListFragment;
import com.example.go4lunch.ui.viewmodel.MainViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeScreenActivity extends PermissionBaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Bundle bundle;
    private MainViewModel mainViewModel;
    private ActivityHomeScreenBinding binding;


    public HomeScreenActivity() {
    }

    @Override
    Activity getActivity() {
        return HomeScreenActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        binding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mainViewModel = ViewModelFactory.getInstance(this, getActivity().getApplication()).obtainViewModel(MainViewModel.class);
        mainViewModel.onLoadView();
        mainViewModel.state.observe(this, this::render);

        displayFragment(savedInstanceState, MapViewFragment.newInstance(isLocationPermissionGranted()));
        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        configureBottomView();
        configureNavHeader();
    }

    private void configureNavHeader() {
        String userName = FirebaseRepository.getUser().getDisplayName();
        String userEmail = FirebaseRepository.getUser().getEmail();
        Uri userPhotoUrl = FirebaseRepository.getUser().getPhotoUrl();
        View headerView = binding.navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.nav_header_user_name);
        TextView textViewMail = headerView.findViewById(R.id.nav_header_user_mail);
        ImageView imageView = headerView.findViewById(R.id.nav_header_user_photo);
        textView.setText(userName);
        textViewMail.setText(userEmail);

        Glide.with(this)
                .load(userPhotoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }

    private void render(MainPageState mainPageState) {
        if (mainPageState instanceof AutocompleteState) {
            autocompleteLaunch.launch(((AutocompleteState) mainPageState).getIntent());
        }
    }

    private final ActivityResultLauncher<Intent> autocompleteLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(Objects.requireNonNull(result.getData()));
                    mainViewModel.onUpdateMapView(PlaceModel.fromPlace(place, mainViewModel.getLocation()));
                }
            }
    );

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_search) {
            mainViewModel.onOpenAutocomplete(HomeScreenActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.activity_main_drawer_logout) {
            signOutCurrentUser();
        }
        binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void signOutCurrentUser() {
        FirebaseRepository.signOut();
        startLoginActivity();
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

    public void displayFragment(Bundle savedInstanceState, Fragment fragment) {
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.activity_main_frame_layout, fragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void startRestaurantDetailActivity(String placeId) {
        Intent intent = new Intent(this, RestaurantDetailActivity.class);
        intent.putExtra("placeId", placeId);
        startActivity(intent);
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
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

    private void configureDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.activityMainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void configureToolBar() {
        setSupportActionBar(binding.toolbar);
    }

    private void configureNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener(this);
    }
}