package com.example.data.features.dashboard.models.getcard


import com.example.data.features.common.model.orders.ProductWrapper
import com.example.data.features.entry.model.CustomerResponseModel
import com.google.gson.annotations.SerializedName

data class CardResponseData(
    @SerializedName("cartQuantity")
    val cartQuantity: Int? = 0,
    @SerializedName("customerId")
    val customerData: CustomerResponseModel? = CustomerResponseModel(),
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("_id")
    val id: String? = "",
    @SerializedName("product")
    val product: String? = "",
    @SerializedName("products")
    val products: List<ProductWrapper>? = listOf(),
    @SerializedName("__v")
    val v: Int? = 0
)