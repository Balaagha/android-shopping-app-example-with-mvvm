package com.example.data.features.common.model.orders


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("categories")
    val categories: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("currentPrice")
    val currentPrice: Int? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("enabled")
    val enabled: Boolean? = null,
    @SerializedName("fabric")
    val fabric: String? = null,
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("imageUrls")
    val imageUrls: List<String?>? = null,
    @SerializedName("itemNo")
    val itemNo: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("previousPrice")
    val previousPrice: Int? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("__v")
    val v: Int? = null
)