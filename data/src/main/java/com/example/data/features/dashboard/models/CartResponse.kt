package com.example.data.features.dashboard.models

import com.example.data.features.entry.model.CustomerResponseModel

data class CartResponse(
    var _id: String? = null,
    var products: ArrayList<ProductModel>? = null,
    var customerId: CustomerResponseModel
)