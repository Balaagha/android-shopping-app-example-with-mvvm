package com.example.data.base.interceptors

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.example.data.helper.manager.UserDataManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManagerImpl @Inject constructor() : TokenManager {

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    private var _authorizationToken: String? = null
    override val authorizationToken: String? get() = _authorizationToken

    override fun setAuthorizationToken(token: String?) {
        Log.d("myTag","token: $token in setAuthorizationToken")
        _authorizationToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.header(AUTHORIZATION_HEADER, "$authorizationToken")
        return chain.proceed(request.build())
    }

}