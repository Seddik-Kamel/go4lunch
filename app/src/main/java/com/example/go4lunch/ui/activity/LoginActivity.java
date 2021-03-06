package com.example.go4lunch.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch.R;
import com.example.go4lunch.databinding.ActivityMainBinding;
import com.example.go4lunch.ui.viewmodel.AuthenticationViewModel;
import com.example.go4lunch.ui.viewmodel.ViewModelFactory;
import com.example.go4lunch.ui.viewmodel.WorkMateViewModel;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends BaseActivity<ActivityMainBinding> {

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(), this::handleResponseAfterSignIn
    );
    private AuthenticationViewModel authenticationViewModel;
    private WorkMateViewModel workMateViewModel;

    @Override
    public ActivityMainBinding getViewBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    Activity getActivity() {
        return LoginActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
        setupListener();
    }

    private void setupViewModel() {
        authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
        workMateViewModel = ViewModelFactory.getInstance(getApplicationContext(), getApplication()).obtainViewModel(WorkMateViewModel.class);
    }

    private void setupListener() {
        if (authenticationViewModel.isCurrentUserLogged()) {
            startHomeActivity();
        } else
            startSignInActivity();
    }

    private void startSignInActivity() {
        signInLauncher.launch(authenticationViewModel.createSignInIntent());
    }

    private void handleResponseAfterSignIn(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
            //Create user in Firestore
            workMateViewModel.persistUser().addOnCompleteListener(task -> startHomeActivity()).addOnFailureListener(e -> Log.e("save_user", e.getMessage()));
        } else {
            if (response == null) {
                showSnackBarr(getString(R.string.login_canceled));
            } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                showSnackBarr(getString(R.string.no_connection));
            } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                showSnackBarr(getString(R.string.unknow_error));
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