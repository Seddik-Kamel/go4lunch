package com.example.go4lunch.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivitySettingsBinding;
import com.example.go4lunch.utils.Preferences;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding> {
    @Override
    ActivitySettingsBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_settings);
    }

    @Override
    Activity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Preferences preferences = new Preferences(getApplicationContext());
        binding.settingNotificationSwitch.setChecked(preferences.getBoolean(Preferences.NOTIFICATION_ENABLE));
        binding.settingNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> preferences.setBoolean(Preferences.NOTIFICATION_ENABLE, isChecked));
    }
}
