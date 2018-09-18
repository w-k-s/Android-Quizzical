package com.asfour.utils

import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.Single
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket



class ConnectivityAssistant(val context: Context) {

    fun hasInternetConnection(): Single<Boolean> {
        return Single.fromCallable {
            try {

                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                socket.connect(socketAddress, timeoutMs)
                socket.close()

                true
            } catch (e: IOException) {
                false
            }
        }
    }

    fun hasNetworkConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }


}