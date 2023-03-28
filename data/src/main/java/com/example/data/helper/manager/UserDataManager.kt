package com.example.data.helper.manager

interface UserDataManager  {
    fun saveUserEmailAndPassword(userNameValue: String?, passwordValue: String?, isEncrypt: Boolean = false)
    fun getUserEmail(): String?
    fun getUserPassword(): String?
    fun saveApiToken(apiTokenValue: String?, isEncrypt: Boolean = false)
    fun setApiToken(): Boolean
    fun clearUserData()
    fun checkUserIsLogin(): Boolean
    fun getPhoneNumber(): String?
}