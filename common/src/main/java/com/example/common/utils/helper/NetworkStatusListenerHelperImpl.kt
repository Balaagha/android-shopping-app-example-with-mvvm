package com.example.common.utils.helper

import android.content.Context
import android.net.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.data.base.commonimpl.NetworkStatusListenerHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkStatusListenerHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
    ) : ConnectivityManager.NetworkCallback(), NetworkStatusListenerHelper {

    private val isNetworkAvailable = MutableStateFlow(false)

    override fun checkNetworkAvailability() = isNetworkAvailable.asSharedFlow().shareIn(
        ProcessLifecycleOwner.get().lifecycleScope, SharingStarted.WhileSubscribed()
    )

    override fun getNetworkAvailabilityStatus() = isNetworkAvailable.value

    override fun init(): MutableStateFlow<Boolean> {

        /**
         * Other way for to get connectivityManager => context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
         */
        val connectivityManager = context.connectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        } else {
            /**
             * Automatically start a download once an internet connection is established
             * could filter using .addCapability(int) or .addTransportType(int) on Builder [networkChangeFilterBuilder]
             */
            val networkChangeFilterBuilder = NetworkRequest.Builder()
            val networkChangeFilter = networkChangeFilterBuilder.build()

            connectivityManager.registerNetworkCallback(networkChangeFilter, this)
        }

        var isConnected = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            val network = connectivityManager.activeNetwork

            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                }
            }
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.let {
                isConnected = it.isConnected
            } ?: run {
                connectivityManager.allNetworks.forEach { network ->
                    val networkCapability = connectivityManager.getNetworkCapabilities(network)
                    networkCapability?.let {
                        if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                            isConnected = true
                            return@forEach
                        }
                    }
                }
            }
        }



        isNetworkAvailable.value = isConnected

        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        Log.d("myTag", "call onAvailable")
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        Log.d("myTag", "call onLost")
        isNetworkAvailable.value = false
    }

    private val Context.connectivityManager: ConnectivityManager
        get() =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

}