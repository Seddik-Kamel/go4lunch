package com.example.go4lunch.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityHomeScreenBinding;
import com.example.go4lunch.infrastructure.repository.FirebaseRepository;

public class HomeScreenActivity extends BaseActivity<ActivityHomeScreenBinding> {

    @Override
    ActivityHomeScreenBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_home_screen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.signOut.setOnClickListener(v -> FirebaseRepository.getAuthUi()
        .signOut(this)
        .addOnCompleteListener(task -> {
            Log.d("test", "Deconnecté");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }));
    }
}