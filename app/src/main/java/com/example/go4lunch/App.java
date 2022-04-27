package com.example.go4lunch;

import android.app.Application;

import com.example.go4lunch.utils.job.workmanager.WorkerManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WorkerManager.getInstance().enqueueResetJob(getApplicationContext());
    }
}
