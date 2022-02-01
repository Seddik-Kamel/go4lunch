package com.example.go4lunch.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.go4lunch.ui.activity.HomeScreenActivity;
import com.google.android.gms.maps.OnMapReadyCallback;

public abstract class GoogleMapBaseFragment extends Fragment implements OnMapReadyCallback {


    abstract int getActionBarTitle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarTitle();
        disableOnBackPressed();
    }

    private void setActionBarTitle() {
        if (getActivity() != null)
            ((HomeScreenActivity) getActivity()).setActionBarTitle(getActionBarTitle());
    }

    protected void disableOnBackPressed() {
        if (getActivity() != null) getActivity().onBackPressed();
    }
}
