package com.example.data.features.common.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.models.EmptyRequest
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.common.model.orders.GetOrdersResponseModel
import com.example.data.features.common.repository.CommonFlowRepository
import retrofit2.Response
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val repository: CommonFlowRepository
) : BaseUseCaseForNetwork<GetOrdersResponseModel, EmptyRequest>() {

    override suspend fun run(params: EmptyRequest): DataWrapper<Response<GetOrdersResponseModel>> {
        return repository.getOrders()
    }

}