package com.example.data.features.common.model


import com.google.gson.annotations.SerializedName

data class AddProductRequestModel(
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("categories")
    val categories: String? = null,
    @SerializedName("color")
    val color: String? = null,
    @SerializedName("currentPrice")
    val currentPrice: Double? = null,
    @SerializedName("previousPrice")
    val previousPrice: Int? = null,
    @SerializedName("imageUrls")
    val imageUrls: List<String?>? = null,
    @SerializedName("myCustomParam")
    val myCustomParam: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("productUrl")
    val productUrl: String? = null,
    @SerializedName("quantity")
    val quantity: Int? = null,
    @SerializedName("enabled")
    var enabled: Boolean? = null,
    var id: String? = null
)