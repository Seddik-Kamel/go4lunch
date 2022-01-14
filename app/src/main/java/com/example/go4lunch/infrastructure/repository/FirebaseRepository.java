package com.example.go4lunch.infrastructure.repository;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

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
}
