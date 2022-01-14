package com.example.go4lunch.ui.viewmodel;

import android.content.Intent;

import androidx.lifecycle.ViewModel;

import com.example.go4lunch.R;
import com.example.go4lunch.infrastructure.repository.FirebaseRepository;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AuthenticationViewModel extends ViewModel {

    public Intent createSignInIntent() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build()
        );

        return FirebaseRepository.getAuthUi()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.AppTheme_NoTitle)
                /*.setIsSmartLockEnabled(false)*/
                .build();
    }


    public FirebaseUser getCurrentUser() {
        return FirebaseRepository.getAuth().getCurrentUser();
    }

    public boolean isCurrentUserLogged() {
        return getCurrentUser() != null;
    }

}
