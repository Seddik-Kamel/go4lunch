package com.example.go4lunch.ui.fragments;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.state.MainPageState;
import com.example.go4lunch.state.NearRestaurantUpdateState;
import com.example.go4lunch.ui.recyclerView.adapters.RestaurantsAdapter;
import com.example.go4lunch.ui.viewmodel.MainViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.List;

public class ListViewRestaurantsFragment extends BaseFragment {

    private MainViewModel mainViewModel;
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

        mainViewModel = ViewModelFactory.getInstance(requireContext(), getActivity().getApplication()).obtainViewModel(MainViewModel.class);

        initRecyclerView();
        mainViewModel.state.observe(requireActivity(), this::render);

        mainViewModel.onLoadView();

        return view;
    }

    private void render(MainPageState mainPageState) {
        if (mainPageState instanceof NearRestaurantUpdateState) {
            lastKnowLocation = ((NearRestaurantUpdateState) mainPageState).getCurrentLocation();
            restaurantList.clear();
            restaurantList.addAll(((NearRestaurantUpdateState) mainPageState).getRestaurantModelArrayList());
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
        mainViewModel.stopLocationUpdate();
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
        adapter = new RestaurantsAdapter(this.restaurantList, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }


}