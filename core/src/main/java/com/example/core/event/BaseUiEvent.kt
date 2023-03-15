package com.example.core.event

import androidx.annotation.StringRes

sealed class BaseUiEvent {
    class SnackBar(val title: String? = null) : BaseUiEvent()
    class Toast(val title: String? = null) : BaseUiEvent()
    class Alert(
        val title: String? = null,
        val message: String? = null,
        @StringRes val titleRes: Int? = null,
        @StringRes val messageRes: Int? = null
    ) : BaseUiEvent()
}
