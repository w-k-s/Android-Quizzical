package com.asfour.application;

import android.app.Application;
import android.util.Log;

import com.asfour.utils.FontsOverride;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by Waqqas on 30/06/15.
 */
public class App extends Application {

    public static interface Extras{
        String Category = "com.asfour.extras.category";
        String Categories = "com.asfour.extras.categories";
        String Questions = "com.asfour.extras.questions";
        String Quiz = "com.asfour.extras.quiz";
        String Score = "com.asfour.extras.score";
    }

    public static interface Observables{
        String Categories = "com.asfour.observables.categories";
        String Questions = "com.asfour.observables.questions";
    }

    private static final String TAG = App.class.getSimpleName();
    private static final long CONNECTION_TIMEOUT_MILLIS = 2 * 60 * 1000;//2 minutes
    private static final String CACHE_DIR_NAME = "http-cache";
    private static final long CACHE_SIZE = 5 * 1024 * 1024; // 5 MB Cache

    private OkHttpClient mClient;

    @Override
    public void onCreate() {
        super.onCreate();

        setFontsOverride();
        configureOkHttpClient();
    }

    public final OkHttpClient getOkHttpClent(){
        return mClient;
    }

    public void setFontsOverride(){
        FontsOverride.setDefaultFont(this, "DEFAULT", "ArchitectsDaughter.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "ArchitectsDaughter.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "ArchitectsDaughter.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "ArchitectsDaughter.ttf");
    }

    private final void configureOkHttpClient(){

        mClient = new OkHttpClient();
        mClient.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        try{
            File cacheDir = new File(getCacheDir(),CACHE_DIR_NAME);
            Cache cache = new Cache(cacheDir,CACHE_SIZE);
            mClient.setCache(cache);
        }catch(Exception e){
            Log.e(TAG, "Cahce directory could not be set",e);
        }
    }
}
