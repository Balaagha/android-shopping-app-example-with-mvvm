package com.example.data.features.dashboard.models

data class CreateWishList(
    val customerId: String? = null,
    val date: String? = null,
    val products: ArrayList<String>? = null
)