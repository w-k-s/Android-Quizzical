package com.asfour.modules

import android.app.Application
import android.content.Context
import com.asfour.application.App
import com.asfour.utils.ConnectivityAssistant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

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

    @Provides
    @Singleton
    fun provideConnectivityAssistant(context: Context) = ConnectivityAssistant(context)
}
