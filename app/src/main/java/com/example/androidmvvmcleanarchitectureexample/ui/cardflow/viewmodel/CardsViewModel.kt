package com.example.androidmvvmcleanarchitectureexample.ui.cardflow.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.common.model.orders.ProductWrapper
import com.example.data.features.dashboard.models.*
import com.example.data.features.dashboard.models.getcard.CardResponseData
import com.example.data.features.dashboard.models.updatecatd.UpdateCardProduct
import com.example.data.features.dashboard.models.updatecatd.UpdateCardRequestModel
import com.example.data.features.dashboard.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class CardsViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    savedStateHandle: SavedStateHandle,
    private val applicationData: Application
) : BaseViewModel(
    savedStateHandle = savedStateHandle,
    application = applicationData
) {

    private val _cardsUiData: MutableStateFlow<CardResponseData?> = MutableStateFlow(null)
    val cardsUiData = _cardsUiData.asStateFlow()

    private val _productUiData: MutableStateFlow<List<ProductWrapper>?> = MutableStateFlow(null)
    val productUiData = _productUiData.asStateFlow()

    private val _selectedProductIdsData: MutableStateFlow<ArrayList<String>?> = MutableStateFlow(null)
    val selectedProductIdsData = _selectedProductIdsData.asStateFlow()

    fun getCart() {
        getCartUseCase.execute(
            params = EmptyRequest,
            successOperation = {
                val mProductWrapperList = mutableListOf<ProductWrapper>()
                val response = it.invoke()
                _cardsUiData.value = response
                response?.products?.forEach { productWrapper ->
                    if(productWrapper.cartQuantity > 0) {
                        mProductWrapperList.add(productWrapper)
                    }
                }
                _productUiData.value = mProductWrapperList
            }
        )
    }

    fun decreaseQuantity(item: ProductWrapper) {
        _productUiData.value.let { productWrapperList ->
            productWrapperList?.forEach { productWrapper ->
                if(productWrapper.product?.id == item.product?.id) {
                    if(productWrapper.cartQuantity > 1) {
                        productWrapper.cartQuantity = productWrapper.cartQuantity - 1
                    }
                }
            }
            Log.d("CardsFragment", "decreaseQuantity: ${productWrapperList}")
            _productUiData.value = null
            _productUiData.value = productWrapperList
        }
    }

    fun increaseQuantity(item: ProductWrapper) {
        _productUiData.value.let { productWrapperList ->
            productWrapperList?.forEach { productWrapper ->
                if(productWrapper.product?.id == item.product?.id && productWrapper.cartQuantity < (item.product?.quantity ?: 0)) {
                    productWrapper.cartQuantity = productWrapper.cartQuantity + 1
                }
            }
            _productUiData.value = null
            _productUiData.value = productWrapperList
        }

    }

    fun updateSelectedProductData(itemID: String, checked: Boolean) {
        val mList = _selectedProductIdsData.value ?: arrayListOf()
        if(checked){
            mList.add(itemID)
        } else {
            mList.remove(itemID)
        }
        _selectedProductIdsData.value = null
        _selectedProductIdsData.value = mList
    }

    fun calculateTotalPrice(): Int {
        var totalPrice = 0
        _productUiData.value?.forEach { productWrapper ->
            if(_selectedProductIdsData.value?.contains(productWrapper.product?.id ?: "") == true) {
                totalPrice += (productWrapper.product?.currentPrice ?: 0) * productWrapper.cartQuantity
            }
        }
        return totalPrice
    }

    fun deleteSelectedProducts() {
        val list =  arrayListOf<ProductWrapper>()
        _productUiData.value?.forEach {
            if(_selectedProductIdsData.value?.contains(it.product?.id ?: "") == false) {
                list.add(it)
            }
        }
        _selectedProductIdsData.value = null
        _productUiData.value = null
        _productUiData.value = list
        updateCards()
        Log.d("CardsFragment", "size: ${_selectedProductIdsData.value?.size} |_selectedProductIdsData.value => ${_selectedProductIdsData.value}")
    }

    fun updateCardForPlaceOrder() {
        val list = arrayListOf<UpdateCardProduct>()
        _productUiData.value?.forEach { productWrapper ->
            if(_selectedProductIdsData.value?.contains(productWrapper.product?.id ?: "") == true) {
                list.add(
                    UpdateCardProduct(
                        cartQuantity = productWrapper.cartQuantity,
                        product = productWrapper.product?.id ?: "",
                    )
                )
            }
        }
        updateCartUseCase.execute(
            params = UpdateCardRequestModel(
                products = list),
            successOperation = {
                placeOrder()
                Log.d("CardsFragment", "updateCartUseCase: ${it.invoke()}")
            }
        )
    }

    fun updateCards() {
        val list = arrayListOf<UpdateCardProduct>()
        _productUiData.value?.forEach { productWrapper ->
            list.add(
                UpdateCardProduct(
                    cartQuantity = productWrapper.cartQuantity,
                    product = productWrapper.product?.id ?: "",
                )
            )
        }
        updateCartUseCase.execute(
            params = UpdateCardRequestModel(products = list),
            successOperation = {
                Log.d("CardsFragment", "updateCartUseCase: ${it.invoke()}")
            }
        )
    }

    fun placeOrder(){
        val createOrder = CreateOrder(
            letterSubject = "Thank you for order! You are welcome!",
            letterHtml = "Thank you for order! You are welcome!",
            shipping = "Kiev 50UAH",
            paymentInfo = "Credit card",
            status = "not shipped",
            email = _cardsUiData.value?.customerData?.email ?: "",
            mobile = _cardsUiData.value?.customerData?.telephone ?: "",
            customerId = _cardsUiData.value?.customerData?.id ?: ""
        )
        createOrderUseCase.execute(
            params = createOrder,
            successOperation = {
                _cardsUiData.value = null
                _productUiData.value = listOf()
                _selectedProductIdsData.value = arrayListOf()
                deleteCartUseCase.invoke(EmptyRequest)
            }
        )
    }

}
