package com.asfour.application

import android.app.Application
import android.util.Log.INFO
import com.asfour.BuildConfig
import com.asfour.modules.*
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

/**
 * Created by Waqqas on 30/06/15.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        } else {
            Fabric.with(this, Crashlytics())
            Crashlytics.log(INFO, javaClass.simpleName, "Fabric Activated")
        }

        initDependencyInjection()
    }

    private fun initDependencyInjection() {
        startKoin(this, listOf(
                apiModules,
                databaseModule,
                dataSourceModule,
                repositoryModule,
                viewModelModule,
                appModule
        ))
    }

}
