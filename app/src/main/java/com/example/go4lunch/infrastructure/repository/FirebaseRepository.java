package com.example.go4lunch.infrastructure.repository;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseRepository {

    private static FirebaseAuth AUTH;
    private static AuthUI AUTH_UI;

    public static FirebaseAuth getAuth(){
        if (AUTH == null)
            AUTH = FirebaseAuth.getInstance();
        return AUTH;
    }

    public static AuthUI getAuthUi(){
        if (AUTH_UI == null) {
            AUTH_UI = AuthUI.getInstance();
        }
        return AUTH_UI;
    }

    public static FirebaseUser getUser(){
        return getAuth().getCurrentUser();
    }

    public static void signOut(){
        getAuth().signOut();
    }

    @Nullable
    public static FirebaseUser getCurrentUser() {
        return FirebaseRepository.getAuth().getCurrentUser();
    }

    @Nullable
    public static String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

}
