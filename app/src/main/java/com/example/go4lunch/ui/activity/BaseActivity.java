package com.example.go4lunch.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewbinding.ViewBinding;

import com.example.go4lunch.utils.eventBus.LocationEvent;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {

    protected T binding;
    protected View view;
    protected Activity activity;


    abstract T getViewBinding();
    abstract Activity getActivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initActivity();
    }

    private void initBinding() {
        binding = getViewBinding();
        view = binding.getRoot();
        setContentView(view);
    }

    private void initActivity() {
        activity = getActivity();
    }

}
