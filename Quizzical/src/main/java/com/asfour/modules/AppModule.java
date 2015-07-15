package com.asfour.modules;

import android.app.Application;

import com.asfour.application.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Waqqas on 15/07/15.
 */
@Module
public class AppModule {

    private App mApp;

    public AppModule(App app){
        mApp = app;
    }

    @Provides
    @Singleton
    Application provideApplication(){
        return mApp;
    }
}
