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

import com.example.go4lunch.R;
import com.example.go4lunch.ui.recyclerView.adapters.RestaurantsAdapter;
import com.example.go4lunch.ui.viewmodel.MapViewModel;

public class ListViewRestaurantsFragment extends BaseFragment {

    private MapViewModel mapViewModel;
    RecyclerView recyclerView;
    private RestaurantsAdapter adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_view_restaurants_fragment, container, false);
        recyclerView = view.findViewById(R.id.list_restaurant);

        return view;
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

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }

    private void initList() {
        adapter = new RestaurantsAdapter(mapViewModel.getListRestaurants());
        recyclerView.setAdapter(adapter);
    }
}