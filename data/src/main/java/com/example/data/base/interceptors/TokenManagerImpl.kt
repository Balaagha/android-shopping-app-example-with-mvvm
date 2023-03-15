package com.example.data.base.interceptors

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
        _authorizationToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.header(AUTHORIZATION_HEADER, "$authorizationToken")
        return chain.proceed(request.build())
    }

}