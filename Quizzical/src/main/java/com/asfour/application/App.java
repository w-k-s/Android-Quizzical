package com.asfour.application;

import android.app.Application;

import com.asfour.api.QuizzicalApi;
import com.asfour.component.AppComponent;
import com.asfour.component.DaggerAppComponent;
import com.asfour.modules.ApiModule;
import com.asfour.modules.AppModule;
import com.asfour.modules.ConfigModule;
import com.asfour.utils.FontsOverride;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Waqqas on 30/06/15.
 */
public class App extends Application {


    private static final String TAG = App.class.getSimpleName();
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);

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
                .configModule(new ConfigModule())
                .apiModule(new ApiModule())
                .build();

    }


    public static AppComponent component(){
        return mAppComponent;
    }
}
