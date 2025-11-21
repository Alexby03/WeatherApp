package com.example.weatherapp.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import com.example.weatherapp.business.ConnectivityObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkConnectivityObserver (
    context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                Log.d("ConnectivityObserver", "Network available")
                trySend(ConnectivityObserver.Status.AVAILABLE)
            }

            override fun onLost(network: Network) {
                Log.d("ConnectivityObserver", "Network lost")
                trySend(ConnectivityObserver.Status.LOST)
            }
        }

        val request = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(request, callback)

        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        if (capabilities != null) {
            Log.d("ConnectivityObserver", "Initial state: AVAILABLE")
            trySend(ConnectivityObserver.Status.AVAILABLE)
        } else {
            Log.d("ConnectivityObserver", "Initial state: UNAVAILABLE")
            trySend(ConnectivityObserver.Status.UNAVAILABLE)
        }

        awaitClose {
            Log.d("ConnectivityObserver", "Unregistering callback")
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }


}