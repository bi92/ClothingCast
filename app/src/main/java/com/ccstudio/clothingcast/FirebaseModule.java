package com.ccstudio.clothingcast;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FirebaseModule {

    @Singleton
    @Provides
    MyFirebase provideMyFirebase(){
        return new MyFirebase();
    }


}
