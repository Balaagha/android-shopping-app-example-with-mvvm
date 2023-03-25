package com.example.data.features.entry.model


import com.google.gson.annotations.SerializedName

data class CustomerLoginResponseModel(
    @SerializedName("success")
    val success: Boolean? = null,
    @SerializedName("token")
    val token: String? = null
)