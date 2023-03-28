package com.example.data.features.dashboard.models

data class CreateOrder(
    val letterSubject: String? = null,
    val letterHtml: String? = null,
    val shipping: String? = null,
    val paymentInfo: String? = null,
    val status: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    val products: ArrayList<OrderContent>? = null
)