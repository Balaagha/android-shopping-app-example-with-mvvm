package com.example.data.di

import com.example.data.base.interceptors.TokenManager
import com.example.data.base.interceptors.TokenManagerImpl
import com.example.data.features.common.repository.CommonFlowRepository
import com.example.data.features.common.repository.CommonFlowRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {


    @Singleton
    @Binds
    abstract fun bindTokenManager(tokenManager: TokenManagerImpl): TokenManager

    @Singleton
    @Binds
    abstract fun bindCommonFlowRepository(repo: CommonFlowRepositoryImpl): CommonFlowRepository


}