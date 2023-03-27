package com.example.data.features.common.model


import com.google.gson.annotations.SerializedName

data class AddProductResponseModel(
    @SerializedName("brand")
    val brand: String? = null,
    @SerializedName("categories")
    val categories: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("currentPrice")
    val currentPrice: Double? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("enabled")
    val enabled: Boolean? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("imageUrls")
    val imageUrls: List<String?>? = null,
    @SerializedName("itemNo")
    val itemNo: String? = null,
    @SerializedName("myCustomParam")
    val myCustomParam: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("previousPrice")
    val previousPrice: Int? = null,
    @SerializedName("productUrl")
    val productUrl: String? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("__v")
    val v: Int? = null
)