package com.asfour.utils

import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


class ConnectivityAssistant(val context: Context) {

    suspend fun hasInternetConnection(): Boolean {
        return GlobalScope.async {

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

        }.await()

    }

    fun hasNetworkConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
    }


}