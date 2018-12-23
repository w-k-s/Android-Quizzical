package com.asfour.application

import android.app.Application
import com.asfour.modules.*
import com.facebook.stetho.Stetho
import org.koin.android.ext.android.startKoin

/**
 * Created by Waqqas on 30/06/15.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
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
