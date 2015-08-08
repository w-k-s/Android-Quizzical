package com.asfour.modules;

import android.content.Context;

import com.asfour.application.Configuration;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Waqqas on 08/08/15.
 */
@Module
public class ConfigModule {

    @Provides
    @Singleton
    Configuration provideConfiguration(Context context) {
        return new Configuration(context);
    }
}
