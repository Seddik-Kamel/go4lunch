package com.example.go4lunch.utils.job.workmanager;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.go4lunch.utils.job.workmanager.worker.ResetWorker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("CommentedOutCode")
public class WorkerManager {

    private static final String WORKER_NOTIFICATION = "WORKER_NOTIFICATION";
    private static final String WORKER_RESET = "WORKER_RESET";
    private static WorkerManager workerManager;

    public static WorkerManager getInstance() {
        if (workerManager == null) {
            synchronized (WorkerManager.class) {
                if (workerManager == null) {
                    workerManager = new WorkerManager();
                }
            }
        }
        return workerManager;
    }

    /*public void enqueueNotificationJobs(Context context) {

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(computeDelayBeforeNotificationCheck(), TimeUnit.MINUTES)
                .addTag(WORKER_NOTIFICATION)
                .build();

        WorkManager.getInstance(context)
                .enqueueUniqueWork(WORKER_NOTIFICATION, ExistingWorkPolicy.REPLACE, oneTimeWorkRequest);
    }*/


    public void enqueueResetJob(Context context) {
        WorkManager.getInstance(context)
                .enqueueUniqueWork(
                        WORKER_RESET,
                        ExistingWorkPolicy.REPLACE,
                        new OneTimeWorkRequest.Builder(ResetWorker.class)
                                .setInitialDelay(computeDelayBeforeMidnightScheduling(), TimeUnit.MINUTES)
                                .build());
    }

    private long computeDelayBeforeMidnightScheduling() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime nextDayMidnightDateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.MIDNIGHT);

        return ChronoUnit.MINUTES.between(currentDateTime, nextDayMidnightDateTime);
    }

    private long computeDelayBeforeNotificationCheck() {
        return 0;
    }
}
