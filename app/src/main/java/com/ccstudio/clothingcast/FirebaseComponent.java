package com.ccstudio.clothingcast;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = FirebaseModule.class)
public interface FirebaseComponent {
    void inject(SignIn target);
}
