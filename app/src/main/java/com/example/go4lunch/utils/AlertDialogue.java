package com.example.go4lunch.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.example.go4lunch.ui.activity.RestaurantDetailActivity;

public class AlertDialogue {

// TODO a ameliorer
    private void showAlertDialog(Context context, RestaurantDetailActivity.AlertDialogInterface alertDialogInterface, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> alertDialogInterface.doSomething())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
