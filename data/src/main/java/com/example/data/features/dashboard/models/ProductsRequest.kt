package com.example.data.features.dashboard.models

data class ProductsRequest(
    val perPage: Int? = null,
    val startPage: Int? = null,
    var categories: String? = null,
    var itemNo: String? = null

)