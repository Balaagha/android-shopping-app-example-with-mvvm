package com.example.data.features.common.model.orders


import com.google.gson.annotations.SerializedName

data class ProductWrapper(
    @SerializedName("cartQuantity")
    var cartQuantity: Int = 0,
    @SerializedName("_id")
    val id: String? = "",
    @SerializedName("product")
    val product: Product? = Product()
)