package com.example.go4lunch.utils;

import android.app.AlertDialog;
import android.content.Context;

@SuppressWarnings("unused")
public class AlertDialogue {

    public interface AlertDialogInterface {
        void doSomething();
    }

    public static void showAlertDialog(Context context, AlertDialogInterface alertDialogInterface, String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> alertDialogInterface.doSomething())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
}
