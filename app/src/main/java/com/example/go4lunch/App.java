package com.example.go4lunch;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.example.go4lunch.utils.job.DeleteWorkmateJobCreator;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new DeleteWorkmateJobCreator());
    }
}
