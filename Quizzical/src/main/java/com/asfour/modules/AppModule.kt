package com.asfour.modules

import android.app.Application
import android.content.Context

import com.asfour.application.App

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Created by Waqqas on 15/07/15.
 */
@Module
class AppModule(private val mApp: App) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return mApp
    }

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return mApp.applicationContext
    }
}
