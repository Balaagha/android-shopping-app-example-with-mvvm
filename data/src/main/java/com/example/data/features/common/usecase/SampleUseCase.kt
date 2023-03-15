package com.example.data.features.common.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.common.model.SampleResponseModel
import com.example.data.features.common.repository.CommonFlowRepository
import retrofit2.Response
import javax.inject.Inject

class SampleUseCase @Inject constructor(
    private val repository: CommonFlowRepository
) : BaseUseCaseForNetwork<SampleResponseModel, Any>() {

    override suspend fun run(params: Any): DataWrapper<Response<SampleResponseModel>> {
        return repository.getSampleData()
    }

}