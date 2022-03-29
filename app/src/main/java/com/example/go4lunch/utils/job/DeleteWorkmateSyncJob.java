package com.example.go4lunch.utils.job;

import android.util.Log;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.example.go4lunch.model.WorkmateModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.ui.viewmodel.WorkMateViewModel;

import java.util.Calendar;

public class DeleteWorkmateSyncJob extends Job {

    public static final String TAG = "delete_workmate_job";
    private final WorkMateViewModel workMateViewModel;

    public DeleteWorkmateSyncJob() {
        workMateViewModel = ViewModelFactory.getInstance(null, null).obtainViewModel(WorkMateViewModel.class);
    }

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {

        //Delete workmate every day at 2 pm
        deleteWorkmateLikedRestaurant();
        Log.i("delete_workmate_job", Result.SUCCESS.toString());
        return Result.SUCCESS;
    }

    private void deleteWorkmateLikedRestaurant() {
        workMateViewModel.deleteCollection();
    }

    public static void scheduleJob() {
        new JobRequest.Builder(DeleteWorkmateSyncJob.TAG)
                .setExact(getDateToStartJob())
                .build()
                .schedule();
    }

    @NonNull
    private static long getDateToStartJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 14);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();

    }

    private void cancelJob(int jobId) {
        JobManager.instance().cancel(jobId);
    }
}
