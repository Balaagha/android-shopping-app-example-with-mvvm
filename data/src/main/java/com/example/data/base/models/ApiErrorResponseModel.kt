package com.example.data.base.models

import com.google.gson.annotations.SerializedName

data class ApiErrorResponseModel(
    @SerializedName("error")
    var error: String? = null,
    @SerializedName("errorDetails")
    var errorDetails: String? = null,
)
