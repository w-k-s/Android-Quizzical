package com.asfour.modules;

import android.app.Application;
import android.util.Log;

import com.asfour.api.QuizzicalApi;
import com.mobprofs.retrofit.converters.SimpleXmlConverter;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;

/**
 * Created by Waqqas on 15/07/15.
 */
@Module
public class ApiModule {

    private static final String TAG = "ApiModule";
    private static final String QUIZZICAL_API_URL = "http://asfour-quizzical.appspot.com";

    private static final long CONNECTION_TIMEOUT_MILLIS = 2 * 60 * 1000;//2 minutes
    private static final String CACHE_DIR_NAME = "http-cache";
    private static final long CACHE_SIZE = 5 * 1024 * 1024; // 5 MB Cache

    @Provides
    @Singleton
    Endpoint provideEndpoint(){
        return Endpoints.newFixedEndpoint(QUIZZICAL_API_URL);
    }

    @Provides
    @Singleton
    Client provideClient(Application app){
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);

        try {
            File cacheDir = new File(app.getCacheDir(), CACHE_DIR_NAME);
            Cache cache = new Cache(cacheDir, CACHE_SIZE);
            client.setCache(cache);
        } catch (Exception e) {
            Log.e(TAG, "Cahce directory could not be set", e);
        }

        return new OkClient(client);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Client client, Endpoint endpoint){
        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setConverter(new SimpleXmlConverter())
                .setClient(client)
                .build();
    }

    @Provides
    @Singleton
    QuizzicalApi provideQuizzicalApi(RestAdapter restAdapter){
        return restAdapter.create(QuizzicalApi.class);
    }
}
