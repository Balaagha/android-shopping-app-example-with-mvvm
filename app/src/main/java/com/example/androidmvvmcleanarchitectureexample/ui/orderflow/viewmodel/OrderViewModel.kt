package com.example.androidmvvmcleanarchitectureexample.ui.orderflow.viewmodel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.example.androidmvvmcleanarchitectureexample.helper.toCustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.model.CustomerModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.orderflow.model.OrderItemUiModel
import com.example.androidmvvmcleanarchitectureexample.ui.orderflow.model.OrderStatusType
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.AddProductModelUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemType
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model.ProfileMenuItemUiData
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view.AddProductFragment
import com.example.androidmvvmcleanarchitectureexample.ui.profileflow.view.ProfileUpdateFragment
import com.example.common.utils.extentions.getIfExists
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.common.usecase.AddProductsUseCase
import com.example.data.features.common.usecase.GetOrdersUseCase
import com.example.data.features.dashboard.models.CategoryModel
import com.example.data.features.dashboard.usecase.GetCategoriesUseCase
import com.example.data.features.entry.model.CustomerResponseModel
import com.example.data.features.entry.usecase.GetCustomerUseCase
import com.example.data.features.entry.usecase.UpdateCustomerUseCase
import com.example.data.helper.manager.UserDataManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    savedStateHandle: SavedStateHandle,
    private val applicationData: Application
) : BaseViewModel(
    savedStateHandle = savedStateHandle,
    application = applicationData
) {

    private val _onGoingOrderUiData: MutableStateFlow<List<OrderItemUiModel>?> = MutableStateFlow(null)
    val onGoingOrderUiData = _onGoingOrderUiData.asStateFlow()

    private val _competedOrderUiData: MutableStateFlow<List<OrderItemUiModel>?> = MutableStateFlow(null)
    val competedOrderUiData = _competedOrderUiData.asStateFlow()

    private val _canceledOrderUiData: MutableStateFlow<List<OrderItemUiModel>?> = MutableStateFlow(null)
    val canceledOrderUiData = _canceledOrderUiData.asStateFlow()

    fun getUserOrdersData() {
        getOrdersUseCase.execute(
            EmptyRequest,
            successOperation = { ordersData ->
                val onGoingOrders = arrayListOf<OrderItemUiModel>()
                val completedOrders = arrayListOf<OrderItemUiModel>()
                val canceledOrders = arrayListOf<OrderItemUiModel>()
                ordersData.invoke()?.forEach { orderItem ->
                    if (orderItem.canceled == true) {
                        orderItem.products?.forEach { productWrapper ->
                            if ((productWrapper.cartQuantity ?: 0) > 0)
                                canceledOrders.add(
                                    OrderItemUiModel(
                                        imageUrl = productWrapper.product?.imageUrls?.getIfExists(0),
                                        title = productWrapper.product?.name,
                                        subTitle = "US $${productWrapper.product?.currentPrice ?: 0}",
                                        statusText = "Canselled",
                                        statusType = OrderStatusType.CANCELLED,
                                        productId = productWrapper.product?.id
                                    )
                                )

                        }
                    } else if (orderItem.status == "not shipped") {
                        orderItem.products?.forEach { productWrapper ->
                            if ((productWrapper.cartQuantity ?: 0) > 0)
                                onGoingOrders.add(
                                    OrderItemUiModel(
                                        imageUrl = productWrapper.product?.imageUrls?.getIfExists(0),
                                        title = productWrapper.product?.name,
                                        subTitle = "US $${productWrapper.product?.currentPrice ?: 0}",
                                        statusText = "In delivery",
                                        statusType = OrderStatusType.ONGOING,
                                        productId = productWrapper.product?.id
                                    )
                                )

                        }
                    } else {
                        orderItem.products?.forEach { productWrapper ->
                            if ((productWrapper.cartQuantity ?: 0) > 0)
                                completedOrders.add(
                                    OrderItemUiModel(
                                        imageUrl = productWrapper.product?.imageUrls?.getIfExists(0),
                                        title = productWrapper.product?.name,
                                        subTitle = "US $${productWrapper.product?.currentPrice ?: 0}",
                                        statusText = "Completed",
                                        statusType = OrderStatusType.COMPLETED,
                                        productId = productWrapper.product?.id
                                    )
                                )
                        }
                    }
                }
                _onGoingOrderUiData.value = onGoingOrders
                _competedOrderUiData.value = completedOrders
                _canceledOrderUiData.value = canceledOrders
            }
        )
    }

}
