package com.example.data.features.entry.model


import com.google.gson.annotations.SerializedName

data class CustomerResponseModel(
    @SerializedName("avatarUrl")
    val avatarUrl: String? = null,
    @SerializedName("customerNo")
    val customerNo: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("enabled")
    val enabled: Boolean? = null,
    @SerializedName("firstName")
    val firstName: String? = null,
    @SerializedName("gender")
    val gender: String? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("isAdmin")
    val isAdmin: Boolean? = null,
    @SerializedName("lastName")
    val lastName: String? = null,
    @SerializedName("login")
    val login: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("telephone")
    val telephone: String? = null,
    @SerializedName("__v")
    val v: Int? = null
)