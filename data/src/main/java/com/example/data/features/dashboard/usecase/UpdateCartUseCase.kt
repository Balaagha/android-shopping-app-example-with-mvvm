package com.example.data.features.dashboard.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.dashboard.models.getcard.CardResponseData
import com.example.data.features.dashboard.models.updatecatd.UpdateCardRequestModel
import com.example.data.features.dashboard.repository.DashboardRepository
import retrofit2.Response
import javax.inject.Inject

class UpdateCartUseCase @Inject constructor(
    private val repository: DashboardRepository
) : BaseUseCaseForNetwork<CardResponseData, UpdateCardRequestModel>() {

    override suspend fun run(params: UpdateCardRequestModel): DataWrapper<Response<CardResponseData>> {
        return repository.updateCart(params)
    }

}