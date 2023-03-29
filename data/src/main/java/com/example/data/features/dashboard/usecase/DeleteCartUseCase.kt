package com.example.data.features.dashboard.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.models.EmptyRequest
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.dashboard.models.deletecard.DeleteCardResponse
import com.example.data.features.dashboard.repository.DashboardRepository
import retrofit2.Response
import javax.inject.Inject

class DeleteCartUseCase @Inject constructor(
    private val repository: DashboardRepository
) : BaseUseCaseForNetwork<DeleteCardResponse, EmptyRequest>() {

    override suspend fun run(params: EmptyRequest): DataWrapper<Response<DeleteCardResponse>> {
        return repository.deleteCart()
    }

}