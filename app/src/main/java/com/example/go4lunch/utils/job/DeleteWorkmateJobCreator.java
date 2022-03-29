package com.example.go4lunch.utils.job;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class DeleteWorkmateJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case DeleteWorkmateSyncJob.TAG:
                return new DeleteWorkmateSyncJob();
            default:
                return null;
        }
    }
}
