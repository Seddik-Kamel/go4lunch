package com.example.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.BuildConfig;
import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.activity.HomeScreenActivity;
import com.example.go4lunch.ui.recyclerView.adapters.RestaurantsAdapter;
import com.example.go4lunch.ui.viewmodel.MapViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.usecase.NearRestaurantUpdateState;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.List;

public class ListViewRestaurantsFragment extends LocationBaseFragment {

    private MapViewModel mapViewModel;
    private List<RestaurantModel> restaurantList = new ArrayList<>();
    RecyclerView recyclerView;
    private PlacesClient placesClient;


    @Override
    LocationResult initLocationResult() {
        return null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getResourceLayout(), container, false);
        recyclerView = view.findViewById(R.id.list_restaurant);

       /* if (getActivity() != null)
            Places.initialize(getActivity().getApplicationContext(), BuildConfig.API_KEY);
        placesClient = Places.createClient(getActivity());*/

       mapViewModel = ViewModelFactory.getInstance(requireContext()).obtainViewModel(MapViewModel.class);
       mapViewModel.state.observe(requireActivity(), this::render);

        return view;
    }

    private void render(NearRestaurantUpdateState nearRestaurantUpdateState){
        restaurantList.clear();
        restaurantList.addAll(((NearRestaurantUpdateState) nearRestaurantUpdateState).getRestaurantModelArrayList());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null)
            mapViewModel = new ViewModelProvider(getActivity()).get(MapViewModel.class);
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }

    private void initList() {
        RestaurantsAdapter adapter = new RestaurantsAdapter(restaurantList, placesClient,  getActivity());
        recyclerView.setAdapter(adapter);
    }
}