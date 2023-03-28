package com.example.data.features.common.model.orders


import com.google.gson.annotations.SerializedName

data class GetOrdersResponseModelItem(
    @SerializedName("canceled")
    val canceled: Boolean? = false,
    @SerializedName("customerId")
    val customerId: CustomerId? = CustomerId(),
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("_id")
    val id: String? = "",
    @SerializedName("letterHtml")
    val letterHtml: String? = "",
    @SerializedName("letterSubject")
    val letterSubject: String? = "",
    @SerializedName("mobile")
    val mobile: String? = "",
    @SerializedName("orderNo")
    val orderNo: String? = "",
    @SerializedName("paymentInfo")
    val paymentInfo: String? = "",
    @SerializedName("products")
    val products: List<ProductWrapper>? = listOf(),
    @SerializedName("shipping")
    val shipping: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("totalSum")
    val totalSum: Int? = 0,
    @SerializedName("__v")
    val v: Int? = 0
)