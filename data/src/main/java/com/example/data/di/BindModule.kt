package com.example.data.di

import com.example.data.base.interceptors.TokenManager
import com.example.data.base.interceptors.TokenManagerImpl
import com.example.data.features.common.repository.CommonFlowRepository
import com.example.data.features.common.repository.CommonFlowRepositoryImpl
import com.example.data.features.dashboard.repository.DashboardRepository
import com.example.data.features.dashboard.repository.DashboardRepositoryImpl
import com.example.data.features.entry.repository.EntryFlowRepository
import com.example.data.features.entry.repository.EntryFlowRepositoryImpl
import com.example.data.helper.manager.UserDataManager
import com.example.data.helper.manager.UserDataManagerImpl
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
    abstract fun bindUserDataManager(userDataManager: UserDataManagerImpl): UserDataManager

    @Singleton
    @Binds
    abstract fun bindCommonFlowRepository(repo: CommonFlowRepositoryImpl): CommonFlowRepository

    @Singleton
    @Binds
    abstract fun bindEntryFlowRepository(repo: EntryFlowRepositoryImpl): EntryFlowRepository


    @Singleton
    @Binds
    abstract fun bindDashboardRepository(repo: DashboardRepositoryImpl): DashboardRepository


}