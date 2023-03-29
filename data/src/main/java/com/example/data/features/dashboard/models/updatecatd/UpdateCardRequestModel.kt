package com.example.data.features.dashboard.models.updatecatd


import com.google.gson.annotations.SerializedName

data class UpdateCardRequestModel(
    @SerializedName("products")
    val products: List<UpdateCardProduct>? = null
)