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

import com.example.go4lunch.R;
import com.example.go4lunch.ui.recyclerView.adapters.WorkmateLikedRestaurantAdapter;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.ui.viewmodel.WorkMateViewModel;
import com.example.go4lunch.state.WorkMatesUpdateState;

public class WorkmatesListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private WorkMateViewModel workMateViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResourceLayout(), container, false);

        recyclerView = view.findViewById(R.id.workmates_recyclerview);

        workMateViewModel = ViewModelFactory.getInstance(requireContext(), getActivity().getApplication()).obtainViewModel(WorkMateViewModel.class);
        workMateViewModel.onLoadView();
        workMateViewModel.listenWorkmate();
        workMateViewModel.state.observe(getViewLifecycleOwner(), this::workmateRender);

        return view;
    }

    @Override
    int getActionBarTitle() {
        return R.string.fragment_title_workmates;
    }

    @Override
    int getResourceLayout() {
        return R.layout.fragment_workmates_list;
    }

    private void workmateRender(WorkMatesUpdateState workMatesUpdateState) {
        workMateViewModel.setWorkmateList(workMatesUpdateState.getWorkmateModelArrayList());
        initRecyclerView();
    }

    private void initRecyclerView() {
        WorkmateLikedRestaurantAdapter adapter = new WorkmateLikedRestaurantAdapter(workMateViewModel.getWorkmateList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
    }
}