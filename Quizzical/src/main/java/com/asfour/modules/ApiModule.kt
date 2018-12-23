package com.asfour.modules

import android.app.Application
import com.asfour.data.api.QuizzicalApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by Waqqas on 15/07/15.
 */
private const val CACHE_DIR_NAME = "http-cache"
private const val CACHE_SIZE = (5 * 1024 * 1024).toLong() // 5 MB Cache

val apiModules = module {
    single {
        provideClient(androidApplication())
    }

    single{
        provideRetrofit(get())
    }

    single{
        provideQuizzicalApi(get())
    }
}

private fun provideClient(app: Application): OkHttpClient {
    val cacheFile = File(app.cacheDir, CACHE_DIR_NAME)
    val cache = Cache(cacheFile, CACHE_SIZE)

    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(2, TimeUnit.MINUTES)
            .cache(cache)
            .build()
}

private fun provideRetrofit(client: OkHttpClient): Retrofit{
    return Retrofit.Builder()
            .baseUrl("https://1pvdx8s5k7.execute-api.ap-south-1.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(client)
            .build()
}

private fun provideQuizzicalApi(restAdapter: Retrofit): QuizzicalApi {
    return restAdapter.create(QuizzicalApi::class.java)
}


