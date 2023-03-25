package com.example.data.features.entry.model


import com.google.gson.annotations.SerializedName

data class CustomerLoginRequestModel(
    @SerializedName("loginOrEmail")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null
)