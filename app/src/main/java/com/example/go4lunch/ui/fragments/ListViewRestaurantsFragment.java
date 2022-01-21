package com.example.go4lunch.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.go4lunch.R;

public class ListViewRestaurantsFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view_restaurants_fragment, container, false);
    }

    @Override
    int getActionBarTitle() {
        return R.string.fragment_title_list;
    }
}