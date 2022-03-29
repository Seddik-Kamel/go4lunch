package com.example.go4lunch.ui.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.recyclerView.adapters.RestaurantsAdapter;
import com.example.go4lunch.ui.viewmodel.MapViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.usecase.NearRestaurantUpdateState;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListViewRestaurantsFragment extends BaseFragment {

    private MapViewModel mapViewModel;
    private RecyclerView recyclerView;
    private final List<RestaurantModel> restaurantList = new ArrayList<>();
    private RestaurantsAdapter adapter;
    public Location lastKnowLocation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getResourceLayout(), container, false);
        recyclerView = view.findViewById(R.id.list_restaurant);

        if (getActivity() != null)
            Places.initialize(getActivity().getApplicationContext(), BuildConfig.API_KEY);

        mapViewModel = ViewModelFactory.getInstance(requireContext(), getActivity().getApplication()).obtainViewModel(MapViewModel.class);

        initRecyclerView();
        mapViewModel.state.observe(requireActivity(), this::render);
        mapViewModel.onLoadView();

        setHasOptionsMenu(true);

        return view;
    }

    private void render(NearRestaurantUpdateState nearRestaurantUpdateState) {
        if (nearRestaurantUpdateState != null) {
            lastKnowLocation = Objects.requireNonNull(nearRestaurantUpdateState.getCurrentLocation());
            restaurantList.clear();
            restaurantList.addAll(nearRestaurantUpdateState.getRestaurantModelArrayList());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapViewModel.stopLocationUpdate();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    int getActionBarTitle() {
        return R.string.fragment_title_list;
    }

    @Override
    int getResourceLayout() {
        return R.layout.fragment_list_view_restaurants;
    }

    private void initRecyclerView() {
        adapter = new RestaurantsAdapter(this.restaurantList, mapViewModel.getPlaceClient(), getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}