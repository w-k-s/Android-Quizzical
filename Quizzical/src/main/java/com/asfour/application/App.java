package com.asfour.application;

import android.app.Application;

import com.asfour.component.AppComponent;
import com.asfour.component.DaggerAppComponent;
import com.asfour.modules.AppModule;
import com.facebook.stetho.Stetho;

/**
 * Created by Waqqas on 30/06/15.
 */
public class App extends Application {


    private static final String TAG = App.class.getSimpleName();
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        initializeDagger();
    }

    private void initializeDagger(){
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .build();

    }


    public static AppComponent component(){
        return mAppComponent;
    }
}
