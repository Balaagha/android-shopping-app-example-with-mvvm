package com.example.core.view

import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.common.utils.extentions.setStatusBarColorAnyVersion

abstract class BaseActivity: AppCompatActivity() {

    abstract fun showLoadingDialog(): Boolean

    abstract fun hideLoadingDialog(): Boolean

    fun setWindowDecoration(@ColorRes colorRes: Int) {
        window.setStatusBarColorAnyVersion(colorRes)
    }

    open fun logOut() {
        TODO("Not yet implemented")
    }

}