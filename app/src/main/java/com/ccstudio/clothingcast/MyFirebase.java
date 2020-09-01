package com.ccstudio.clothingcast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Provides;

public class MyFirebase {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseStorage defaultStorage;


    public MyFirebase() {
        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }

    FirebaseFirestore getDB(){
        return this.db;
    }

    FirebaseUser getUser() {
        if(this.user != null) return this.user;
        else return mAuth.getCurrentUser();
    }

    FirebaseAuth getmAuth() {
        return this.mAuth;
    }

}
