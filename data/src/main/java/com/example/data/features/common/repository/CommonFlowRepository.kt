package com.example.data.features.common.repository

import android.content.Context
import com.example.data.base.models.DataWrapper
import com.example.data.base.repository.BaseRepository
import com.example.data.features.common.model.SampleResponseModel
import com.example.data.features.common.services.CommonFlowServices
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface CommonFlowRepository {

    suspend fun getSampleData(): DataWrapper<Response<SampleResponseModel>>

}

@Singleton
class CommonFlowRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val services: CommonFlowServices,
) : BaseRepository(context),CommonFlowRepository {


    override suspend fun getSampleData(): DataWrapper<Response<SampleResponseModel>> {
        return launchApiCall {
            services.getSampleData(
                queries = hashMapOf(),
                headers = hashMapOf()
            )
        }
    }

}