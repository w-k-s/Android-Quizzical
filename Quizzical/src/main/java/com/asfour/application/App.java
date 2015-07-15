package com.asfour.application;

import android.app.Application;

import com.asfour.component.AppComponent;
import com.asfour.component.DaggerAppComponent;
import com.asfour.modules.ApiModule;
import com.asfour.modules.AppModule;
import com.asfour.utils.FontsOverride;

/**
 * Created by Waqqas on 30/06/15.
 */
public class App extends Application {

    public static interface Extras {
        String Category = "com.asfour.extras.category";
        String Categories = "com.asfour.extras.categories";
        String Questions = "com.asfour.extras.questions";
        String Quiz = "com.asfour.extras.quiz";
        String Score = "com.asfour.extras.score";
    }

    public static interface Observables {
        String Categories = "com.asfour.observables.categories";
        String Questions = "com.asfour.observables.questions";
    }

    private static final String TAG = App.class.getSimpleName();
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setFontsOverride();
        initializeDagger();
    }

    public void setFontsOverride() {
        FontsOverride.setDefaultFont(this, "DEFAULT", "ArchitectsDaughter.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "ArchitectsDaughter.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "ArchitectsDaughter.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "ArchitectsDaughter.ttf");
    }

    private void initializeDagger(){
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .build();
    }

    public static AppComponent component(){
        return mAppComponent;
    }
}
