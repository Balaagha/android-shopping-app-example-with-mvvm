package com.example.uitoolkit.custom.models

import java.math.BigDecimal

data class ItemModel(
    var percent: String? = null,
    var favouriteIconVisibility: Boolean = false,
    var favouriteIconSelected: Boolean = false,
    var imageurl: String? = null,
    var previousPrice: BigDecimal? = null,
    var currentPrice: BigDecimal? = null
)
