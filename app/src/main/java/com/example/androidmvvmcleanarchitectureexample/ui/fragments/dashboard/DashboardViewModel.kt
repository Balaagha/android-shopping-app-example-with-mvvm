package com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.login.CreateAccountSecondFragment
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.dashboard.models.CategoryModel
import com.example.data.features.dashboard.models.ProductModel
import com.example.data.features.dashboard.models.ProductsRequest
import com.example.data.features.dashboard.usecase.GetCategoriesUseCase
import com.example.data.features.dashboard.usecase.GetProductsUseCase
import com.example.data.features.entry.usecase.CreateAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getProductsUseCase: GetProductsUseCase,
    savedState: SavedStateHandle,
    private val application: Application
) : BaseViewModel(savedState, application) {


    private val categoriesResult = MutableLiveData<List<CategoryModel>>()

    private val productsResult = MutableLiveData<List<ProductModel>>()

    init {
        getCategories()
        getProducts()
    }

    fun getCategoriesResult() : MutableLiveData<List<CategoryModel>> {

        return categoriesResult
    }

    fun getProductsResult() : MutableLiveData<List<ProductModel>> {

        return productsResult
    }


    private fun getCategories() {
        getCategoriesUseCase.execute(
            params = EmptyRequest,
            successOperation = {
                categoriesResult.postValue(it.invoke())
            }
        )
    }

    private fun getProducts() {
        val request = ProductsRequest(perPage = 20,startPage = 1)
        getProductsUseCase.execute(
            params = request,
            successOperation = {
                productsResult.postValue(it.invoke()?.products)
            }
        )
    }

}