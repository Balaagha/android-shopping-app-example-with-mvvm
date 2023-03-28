package com.example.androidmvvmcleanarchitectureexample.ui.fragments.product

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.dashboard.models.*
import com.example.data.features.dashboard.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val createCartUseCase: CreateCartUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    savedState: SavedStateHandle,
    private val application: Application
) : BaseViewModel(savedState, application) {

    private val productResult = MutableLiveData<List<ProductModel>>()

    private val productsResult = MutableLiveData<List<ProductModel>>()

    private val orderResult = MutableLiveData<Boolean>()

    private val cartListResult = MutableLiveData<CartResponse>()

    private val cartListFailResult = MutableLiveData<Boolean>()

    private val addCartResult = MutableLiveData<Boolean>()


    fun getAddToCartResult() : MutableLiveData<Boolean> {

        return addCartResult
    }
    fun getOrderResult() : MutableLiveData<Boolean> {

        return orderResult
    }

    fun getCartListFailResult() : MutableLiveData<Boolean> {

        return cartListFailResult
    }

    fun getCartResult() : MutableLiveData<CartResponse> {

        return cartListResult
    }

    fun getProductResult() : MutableLiveData<List<ProductModel>> {

        return productResult
    }

    fun getProductsResult() : MutableLiveData<List<ProductModel>> {

        return productsResult
    }

    init {
        getProducts()
        getCart()
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

    fun createOrder(orderRequest: CreateOrder) {
        createOrderUseCase.execute(
            params = orderRequest,
            successOperation = {
                orderResult.postValue(true)
            }
        )
    }

    fun createCart(request: CreateCart) {
        createCartUseCase.execute(
            params = request,
            successOperation = {
                 getCart()
            }
        )
    }

    fun addToCart(request: CartRequest) {
        addToCartUseCase.execute(
            params = request,
            successOperation = {
               addCartResult.postValue(true)
            }
        )
    }

    private fun getCart() {
        getCartUseCase.execute(
            params = EmptyRequest,
            successOperation = {
                cartListResult.postValue(it.invoke())
            }, failOperation = {
                cartListFailResult.postValue(true)
            }
        )
    }

}