package com.asfour.modules

import com.asfour.utils.ConnectivityAssistant
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

/**
 * Created by Waqqas on 15/07/15.
 */
val appModule = module {
    single { ConnectivityAssistant(androidContext()) }
}