package com.example.data.helper.manager

import android.app.Application
import android.util.Log
import com.example.data.base.interceptors.TokenManager
import com.example.data.helper.models.typedefs.SharedTypes
import com.example.data.utils.SharedConstant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataManagerImpl @Inject constructor(
    private val applicationContext: Application,
    private val tokenManager: TokenManager
) : UserDataManager {

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    private var userEmail: String? = null
    private var userPassword: String? = null

    override fun saveApiToken(apiTokenValue: String?, isEncrypt: Boolean) {
        val sharedPrefsManager = SharedPrefsManager(applicationContext.applicationContext, SharedTypes.USER_DATA)
        sharedPrefsManager.set(SharedConstant.API_TOKEN, apiTokenValue, isEncrypt)
        Log.d("myTag","token: $apiTokenValue | getApiToken after save=> &${getApiToken()} in getApiToken")
        tokenManager.setAuthorizationToken(apiTokenValue)
    }

    override fun setApiToken(): Boolean {
        val token = getApiToken()
        Log.d("myTag","token: $token in setApiToken")
        tokenManager.setAuthorizationToken(
            getApiToken()
        )
        return token != null && token.isNotEmpty()
    }

    override fun saveUserEmailAndPassword(userNameValue: String?, passwordValue: String?, isEncrypt: Boolean){
        this.userEmail = userNameValue
        this.userPassword = passwordValue
        val sharedPrefsManager = SharedPrefsManager(applicationContext.applicationContext, SharedTypes.USER_DATA)
        sharedPrefsManager.set(SharedConstant.USER_EMAIL, userNameValue,isEncrypt)
        sharedPrefsManager.set(SharedConstant.USER_PASSWORD, passwordValue,isEncrypt)
    }

    override fun getUserEmail(): String? {
        val sharedPrefsManager = SharedPrefsManager(applicationContext.applicationContext, SharedTypes.USER_DATA)
        return sharedPrefsManager.get(SharedConstant.USER_EMAIL, null,true)
    }

    override fun getUserPassword(): String? {
        val sharedPrefsManager = SharedPrefsManager(applicationContext.applicationContext, SharedTypes.USER_DATA)
        return sharedPrefsManager.get(SharedConstant.USER_PASSWORD, null,true)
    }

    private fun getApiToken(): String? {
        val sharedPrefsManager = SharedPrefsManager(applicationContext.applicationContext, SharedTypes.USER_DATA)
        return sharedPrefsManager.get(SharedConstant.API_TOKEN, null,true)
    }
}