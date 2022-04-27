package com.example.go4lunch.utils.job.workmanager.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.example.go4lunch.infrastructure.repository.WorkmateRepository;
import com.example.go4lunch.utils.job.workmanager.WorkerManager;
import com.google.common.util.concurrent.ListenableFuture;

public class ResetWorker extends ListenableWorker {
    private final WorkmateRepository workmateRepository;

    private final WorkerManager workerManager;

    public ResetWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);

        workmateRepository= WorkmateRepository.getInstance();
        workerManager = WorkerManager.getInstance();
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        // Rearm next notification
        workerManager.enqueueResetJob(getApplicationContext());

        return CallbackToFutureAdapter.getFuture(completer -> {
            workmateRepository.deleteWorkmateLikedRestaurantCollection();
            return completer.set(Result.success());
        });
    }
}
