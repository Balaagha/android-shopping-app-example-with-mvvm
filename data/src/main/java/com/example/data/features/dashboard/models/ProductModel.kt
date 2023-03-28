package com.example.data.features.dashboard.models

import java.math.BigDecimal

data class ProductModel(
    val enabled: Boolean? = null,
    val imageUrls: ArrayList<String>? = null,
    val quantity: String? = null,
    val _id: String? = null,
    val name: String? = null,
    val itemNo: String? = null,
    val currentPrice: BigDecimal? = null,
    val previousPrice: BigDecimal? = null,
    val categories: String? = null,
    val date: String? = null,
    val description: String? = null,
): java.io.Serializable