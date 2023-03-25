package com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.core.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val application: Application
) : BaseViewModel(savedState, application) {

}