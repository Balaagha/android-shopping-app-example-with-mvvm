package com.example.androidmvvmcleanarchitectureexample.ui.orderflow.model

data class ProductItemUiModel(
    val imageUrl: String?,
    val title: String?,
    val subTitle: String?,
    val statusText: String?,
    val statusType: OrderStatusType,
    val productId: String?
)
