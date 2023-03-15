package com.example.data.features.common.model


import com.google.gson.annotations.SerializedName

data class SampleResponseModel(
    @SerializedName("amount")
    val amount: String? = null,
    @SerializedName("date")
    val date: String? = null,
    @SerializedName("estimationDetailsList")
    val estimationDetailsList: List<EstimationDetails>? = null
) {
    data class EstimationDetails(
        @SerializedName("amountData")
        val amountData: String? = null,
        @SerializedName("title")
        val title: String? = null
    )
}