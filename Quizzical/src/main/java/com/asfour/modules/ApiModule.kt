package com.asfour.modules

import android.app.Application
import com.asfour.data.api.QuizzicalApi
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Waqqas on 15/07/15.
 */
@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideClient(app: Application): OkHttpClient {
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

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {

        return Retrofit.Builder()
                .baseUrl("http://34.245.141.120:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
    }

    @Provides
    @Singleton
    fun provideQuizzicalApi(restAdapter: Retrofit): QuizzicalApi {
        return restAdapter.create(QuizzicalApi::class.java)
    }

    companion object {
        private val CACHE_DIR_NAME = "http-cache"
        private val CACHE_SIZE = (5 * 1024 * 1024).toLong() // 5 MB Cache
    }
}
