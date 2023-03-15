package com.example.data.base.repository

import android.content.Context
import android.util.Log
import com.example.data.base.commonimpl.NetworkStatusListenerHelper
import com.example.data.base.models.*
import com.example.data.helper.config.AppConfig
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository(
    private var appContext: Context
) {

    var networkStatusListenerHelper: NetworkStatusListenerHelper
    var appConfig: AppConfig
    var mContext: Context

    init {
        val entryPoint =
            EntryPointAccessors.fromApplication(appContext, BaseRepositoryEntryPoint::class.java)
        networkStatusListenerHelper = entryPoint.networkStatusListenerHelper()
        appConfig = entryPoint.appConfig()
        mContext = appContext.applicationContext
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface BaseRepositoryEntryPoint {
        fun networkStatusListenerHelper(): NetworkStatusListenerHelper
        fun appConfig(): AppConfig
    }

    inline fun <reified T> launchApiCall(
        apiCall: () -> Response<T>
    ): DataWrapper<Response<T>> {
        return try {
            if (true) {
                val response = apiCall.invoke()
                DataWrapper.Success(
                    value = response
                )
            } else {
                DataWrapper.Failure(
                    failureType = FailureType.NO_INTERNET_CONNECTION,
                    failureBehavior = FailureBehavior.ALERT
                )
            }
        } catch (throwable: Throwable) {
            Log.d("myTag","ERROR => ${throwable.localizedMessage}")
            handleApiCallException(throwable)
        }
    }

    fun <T> handleApiCallException(throwable: Throwable): DataWrapper<T> {
        when (throwable) {
            is SocketTimeoutException -> {
                return DataWrapper.Failure(FailureType.RESPONSE_TIMEOUT)
            }
            is UnknownHostException -> {
                return DataWrapper.Failure(FailureType.NO_INTERNET_CONNECTION)
            }

            is ConnectException -> {
                return DataWrapper.Failure(FailureType.NO_INTERNET_CONNECTION)
            }
            is HttpException -> {
                when {
                    throwable.code() == 401 -> {
                        val errorResponse = Gson().fromJson(
                            throwable.response()?.errorBody()!!.charStream().readText(),
                            ErrorResponse::class.java
                        )
                        return DataWrapper.Failure(
                            failureType = FailureType.HTTP_EXCEPTION,
                            code = throwable.code(),
                            message = errorResponse.detail
                        )
                    }
                    else -> {
                        return if (throwable.response()?.errorBody()!!.charStream().readText()
                                .isEmpty()
                        ) {
                            DataWrapper.Failure(
                                failureType = FailureType.HTTP_EXCEPTION,
                                code = throwable.code()
                            )
                        } else {
                            try {
                                val errorResponse = Gson().fromJson(
                                    throwable.response()?.errorBody()!!.charStream().readText(),
                                    ErrorResponse::class.java
                                )

                                DataWrapper.Failure(
                                    failureType = FailureType.HTTP_EXCEPTION,
                                    code = throwable.code(),
                                    message = errorResponse.detail
                                )
                            } catch (ex: JsonSyntaxException) {
                                DataWrapper.Failure(
                                    failureType = FailureType.HTTP_EXCEPTION,
                                    code = throwable.code()
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                return DataWrapper.Failure(FailureType.OTHER)
            }
        }
    }


}