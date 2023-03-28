package com.example.androidmvvmcleanarchitectureexample.ui.fragments.wishList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.core.viewmodel.BaseViewModel
import com.example.data.base.models.EmptyRequest
import com.example.data.features.dashboard.models.ProductModel
import com.example.data.features.dashboard.usecase.GetWishListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val getWishListUseCase: GetWishListUseCase,
    savedState: SavedStateHandle,
    private val application: Application
) : BaseViewModel(savedState, application) {

    private val wishListResult = MutableLiveData<List<ProductModel>>()

    init {
        getWishList()
    }

    fun getWishListResult() : MutableLiveData<List<ProductModel>> {

        return wishListResult
    }

    private fun getWishList() {
        getWishListUseCase.execute(
            params = EmptyRequest,
            successOperation = {
                wishListResult.postValue(it.invoke()!!.products!!)
            }
        )
    }
}