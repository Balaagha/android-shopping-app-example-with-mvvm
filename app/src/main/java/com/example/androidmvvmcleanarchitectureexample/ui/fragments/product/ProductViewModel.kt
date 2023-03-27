package com.example.androidmvvmcleanarchitectureexample.ui.fragments.product

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.core.viewmodel.BaseViewModel
import com.example.data.features.dashboard.models.ProductModel
import com.example.data.features.dashboard.models.ProductsRequest
import com.example.data.features.dashboard.usecase.GetProductUseCase
import com.example.data.features.dashboard.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    savedState: SavedStateHandle,
    private val application: Application
) : BaseViewModel(savedState, application) {

    private val productResult = MutableLiveData<List<ProductModel>>()

    private val productsResult = MutableLiveData<List<ProductModel>>()

    fun getProductResult() : MutableLiveData<List<ProductModel>> {

        return productResult
    }

    fun getProductsResult() : MutableLiveData<List<ProductModel>> {

        return productsResult
    }

    init {
        getProducts()
    }

    fun getProduct(itemNo: String) {
        val request = ProductsRequest(itemNo = itemNo)
        getProductUseCase.execute(
            params = request,
            successOperation = {
                productResult.postValue(it.invoke())
            }
        )
    }

    private fun getProducts() {
        val request = ProductsRequest(perPage = 20,startPage = 1)
        getProductsUseCase.execute(
            params = request,
            successOperation = {
                productsResult.postValue(it.invoke()?.products)
            }
        )
    }

}