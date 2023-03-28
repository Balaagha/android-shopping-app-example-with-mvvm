package com.example.androidmvvmcleanarchitectureexample.ui.fragments.products

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.core.viewmodel.BaseViewModel
import com.example.data.features.dashboard.models.*
import com.example.data.features.dashboard.usecase.AddProductToWishListUseCase
import com.example.data.features.dashboard.usecase.CreateOrderUseCase
import com.example.data.features.dashboard.usecase.GetProductsByCategoryIdUseCase
import com.example.data.features.dashboard.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsByCategoryIdUseCase: GetProductsByCategoryIdUseCase,
    private val addProductToWishListUseCase: AddProductToWishListUseCase,
    savedState: SavedStateHandle,
    private val application: Application
) : BaseViewModel(savedState, application) {


    var page = 1

    private val productsResult = MutableLiveData<List<ProductModel>>()

    fun getProductsResult() : MutableLiveData<List<ProductModel>> {

        return productsResult
    }

    fun getProducts(id: String?) {
        isShowBaseLoadingIndicator = false
        val request = ProductsRequest(perPage = 20,startPage = page,categories = id)
        getProductsByCategoryIdUseCase.execute(
            params = request,
            successOperation = {
                productsResult.postValue(it.invoke()?.products)
            }
        )
    }

    fun addProductToWishList(id: String) {
        isShowBaseLoadingIndicator = false
        val wishListRequest = WishListRequest(productId = id)
        addProductToWishListUseCase.execute(
            params = wishListRequest,
            successOperation = {
                //productsResult.postValue(it.invoke())
            }
        )
    }

}