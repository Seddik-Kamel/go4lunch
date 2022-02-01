package com.example.go4lunch.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.ui.viewmodel.AuthenticationViewModel;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(), this::handleResponseAfterSignIn
    );
    AuthenticationViewModel authenticationViewModel;

    @Override
    ActivityMainBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    Activity getActivity() {
         return MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        setupListener();
    }

    private void setupViewModel() {
        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
    }

    private void setupListener() {
        if (authenticationViewModel.isCurrentUserLogged())
            startHomeActivity();
        else
         // startSignInActivity();
        startHomeActivity();
    }

    private void startSignInActivity() {
        signInLauncher.launch(authenticationViewModel.createSignInIntent());
    }

    private void handleResponseAfterSignIn(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();

        // success
        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
            startHomeActivity();
            showSnackBarr("L'authentification a reussi");
        } else {
            if (response == null) {
                showSnackBarr("Authentification annulée");
            } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackBarr("Pas de connexion");
            } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackBarr("Erreur inconnue");
            }
        }
    }

    private void showSnackBarr(String message) {
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }
}