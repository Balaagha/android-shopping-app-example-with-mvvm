package com.example.androidmvvmcleanarchitectureexample.ui.profileflow.model

import androidx.annotation.DrawableRes
import com.example.androidmvvmcleanarchitectureexample.R




enum class ProfileMenuItemType(@DrawableRes val iconResId: Int) {
    ADDRESS(R.drawable.ic_menu_adress),
    NOTIFICATION(R.drawable.ic_menu_notification),
    PAYMENT(R.drawable.ic_menu_payment),
    SECURITY(R.drawable.ic_menu_security),
    PRIVACY(R.drawable.ic_menu_privacy),
    HELP_CENTER(R.drawable.ic_menu_help_center),
    LOG_OUT(R.drawable.ic_menu_log_out);

    companion object {
        fun from(value: Int) = values().firstOrNull { it.iconResId == value } ?: ADDRESS
    }
}

