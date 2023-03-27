package com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model

import androidx.databinding.ObservableField
import com.example.data.features.common.model.AddProductRequestModel

class AddProductModelUiData(
    val description: ObservableField<String> = ObservableField(),
    val currentPrice: ObservableField<String> = ObservableField(),
    val previousPrice: ObservableField<String> = ObservableField(),
    val name: ObservableField<String> = ObservableField(),
    val quantity: ObservableField<String> = ObservableField(),
    val imageUrls: ArrayList<String> = arrayListOf(),
    var categories: String? = null
) {
    fun toAddProductRequestModel(): AddProductRequestModel = AddProductRequestModel(
        description = description.get(),
        categories = categories,
        currentPrice = currentPrice.get()?.toDouble(),
        previousPrice = previousPrice.get()?.toInt(),
        imageUrls = imageUrls,
        name = name.get(),
        quantity = quantity.get()?.toIntOrNull()
    )
}