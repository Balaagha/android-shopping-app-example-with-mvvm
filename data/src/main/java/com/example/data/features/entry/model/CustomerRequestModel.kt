package com.example.data.features.entry.model


import com.google.gson.annotations.SerializedName

data class CustomerRequestModel(
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("isAdmin")
    val isAdmin: Boolean? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("login")
    val userName: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("telephone")
    val telephone: String? = null
)