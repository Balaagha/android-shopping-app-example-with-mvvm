package com.example.data.base.interceptors

import okhttp3.Interceptor

interface TokenManager : Interceptor {

    val authorizationToken: String?

    fun setAuthorizationToken(token: String?)

}