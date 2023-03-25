package com.example.data.features.entry.usecase

import com.example.data.base.models.DataWrapper
import com.example.data.base.usecase.BaseUseCaseForNetwork
import com.example.data.features.entry.model.CustomerLoginRequestModel
import com.example.data.features.entry.model.CustomerLoginResponseModel
import com.example.data.features.entry.repository.EntryFlowRepository
import retrofit2.Response
import javax.inject.Inject

class LoginAccountUseCase @Inject constructor(
    private val repository: EntryFlowRepository
) : BaseUseCaseForNetwork<CustomerLoginResponseModel, CustomerLoginRequestModel>() {

    override suspend fun run(params: CustomerLoginRequestModel): DataWrapper<Response<CustomerLoginResponseModel>> {
        return repository.loginAccount(params)
    }

}