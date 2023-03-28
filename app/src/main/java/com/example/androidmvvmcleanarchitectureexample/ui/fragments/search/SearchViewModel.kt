package com.example.androidmvvmcleanarchitectureexample.ui.fragments.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.core.viewmodel.BaseViewModel
import com.example.data.features.dashboard.models.ProductModel
import com.example.data.features.dashboard.models.ProductsRequest
import com.example.data.features.dashboard.models.SearchRequest
import com.example.data.features.dashboard.usecase.GetProductsUseCase
import com.example.data.features.dashboard.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    savedState: SavedStateHandle,
    private val application: Application
) : BaseViewModel(savedState, application) {

    private val searchResult = MutableLiveData<List<ProductModel>>()

    fun getSearchResult() : MutableLiveData<List<ProductModel>> {

        return searchResult
    }

    fun searchProducts(query: String) {
        isShowBaseLoadingIndicator = false
        val request = SearchRequest(query = query)
        searchProductsUseCase.execute(
            params = request,
            successOperation = {
                searchResult.postValue(it.invoke())
            }
        )
    }
}