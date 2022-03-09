package com.example.go4lunch.ui.fragments;

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
import com.example.go4lunch.ui.recyclerView.adapters.RestaurantsAdapter;
import com.example.go4lunch.ui.viewmodel.MapViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.usecase.NearRestaurantUpdateState;
import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;
import java.util.List;

public class ListViewRestaurantsFragment extends BaseFragment {

    private MapViewModel mapViewModel;
    private RecyclerView recyclerView;
    private final List<RestaurantModel> restaurantList = new ArrayList<>();
    private RestaurantsAdapter adapter;


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

        return view;
    }

    private void render(NearRestaurantUpdateState mainPageState) {
        if (mainPageState != null) {
            restaurantList.clear();
            restaurantList.addAll(mainPageState.getRestaurantModelArrayList());
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
    int getActionBarTitle() {
        return R.string.fragment_title_list;
    }

    @Override
    int getResourceLayout() {
        return R.layout.list_view_restaurants_fragment;
    }

    private void initRecyclerView() {
        adapter = new RestaurantsAdapter(this.restaurantList, mapViewModel.getPlaceClient(), getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}