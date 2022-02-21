package com.example.go4lunch.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.go4lunch.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsRestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsRestaurantsFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailsRestaurantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsRestaurantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsRestaurantsFragment newInstance(String param1, String param2) {
        DetailsRestaurantsFragment fragment = new DetailsRestaurantsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getResourceLayout(), container, false);
    }


    @Override
    int getActionBarTitle() {
        return R.string.details_restaurant_fragment;
    }

    @Override
    int getResourceLayout() {
        return R.layout.fragment_details_restaurants;
    }

   /* @Override
    protected void disableOnBackPressed() {
        super.disableOnBackPressed();
        if (getActivity() != null) getActivity()
    }*/
}