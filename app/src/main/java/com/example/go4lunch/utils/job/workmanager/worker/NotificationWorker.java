package com.example.go4lunch.utils.job.workmanager.worker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.go4lunch.R;
import com.example.go4lunch.ui.activity.SettingsActivity;
import com.example.go4lunch.utils.Preferences;
import com.example.go4lunch.utils.job.workmanager.WorkerManager;
import com.google.common.util.concurrent.ListenableFuture;

public class NotificationWorker extends ListenableWorker {

    private final WorkerManager workerManager;
    private static final String NOTIFICATION_CHANNEL = "NOTIFICATION_CHANNEL";
    private final int CHANNEL_CODE = 1;
    private Preferences preferences;

    public NotificationWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        workerManager = WorkerManager.getInstance();
        preferences = new Preferences(appContext);
    }

    private PendingIntent getPendingIntent() {
        return PendingIntent.getActivity(
                getApplicationContext(),
                CHANNEL_CODE,
                new Intent(getApplicationContext(), SettingsActivity.class),
                PendingIntent.FLAG_IMMUTABLE);
    }

    private void sendNotification(NotificationCompat.Builder notificationBuilder) {
        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(getNotificationChannel());
            notificationManager.notify(CHANNEL_CODE, notificationBuilder.build());
        }
    }

    @NonNull
    private NotificationChannel getNotificationChannel() {
        String NOTIFICATION_CHANNEL_NAME = "It's time to eat!";
        NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        channel.setDescription("CHANNEL DESCRIPTION");
        return channel;
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {

        boolean notificationEnable = preferences.getBoolean(Preferences.NOTIFICATION_ENABLE);

        if (notificationEnable){
            workerManager.enqueueNotificationJobs(getApplicationContext());

            return CallbackToFutureAdapter.getFuture(completer -> {
                String notificationText = "Il est l'heure d'aller manger";

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                        getApplicationContext(), NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.baseline_restaurant_24)
                        .setContentTitle(getApplicationContext().getString(R.string.app_name))
                        .setContentText(notificationText)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                        .setContentIntent(getPendingIntent())
                        .setAutoCancel(true);

                sendNotification(notificationBuilder);
                completer.set(Result.success());

                return null;
            });
        }
        return null;
    }
}
