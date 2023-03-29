package com.example.data.features.dashboard.models.updatecatd


import com.google.gson.annotations.SerializedName

data class UpdateCardProduct(
    @SerializedName("cartQuantity")
    val cartQuantity: Int? = null,
    @SerializedName("product")
    val product: String? = null
)