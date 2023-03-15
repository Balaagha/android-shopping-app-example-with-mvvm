package com.example.androidmvvmcleanarchitectureexample.di

import com.example.androidmvvmcleanarchitectureexample.helper.config.AppConfigImpl
import com.example.common.utils.helper.NetworkStatusListenerHelperImpl
import com.example.data.base.commonimpl.NetworkStatusListenerHelper
import com.example.data.helper.config.AppConfig
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindModule {

    @Binds
    @Singleton
    abstract fun bindNetworkStatusListenerHelper(configImpl: NetworkStatusListenerHelperImpl): NetworkStatusListenerHelper

    @Binds
    @Singleton
    abstract fun bindAppConfig(configImpl: AppConfigImpl): AppConfig

}
