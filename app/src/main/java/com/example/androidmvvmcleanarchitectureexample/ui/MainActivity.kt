package com.example.androidmvvmcleanarchitectureexample.ui

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.ActivityMainBinding
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.activity.LoginActivity
import com.example.core.helper.viewInflateBinding
import com.example.core.view.BaseActivity
import com.example.data.helper.manager.UserDataManager
import com.example.uitoolkit.loading.LoadingPopup
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by viewInflateBinding(ActivityMainBinding::inflate)
    private lateinit var navController: NavController

    @Inject
    lateinit var userDataManager: UserDataManager

    private val loadingDialog: LoadingPopup? by lazy {
        LoadingPopup.getInstance(this).customLayoutLoading()
            .setCustomLottieAnimation("Loading.json",
                android.R.color.background_dark)
            .noIntentionalDelay()
            .setBackgroundOpacity(40)
            .cancelable(false)
            .setCustomizationBlock { context, rootView ->
//                Glide.with(context).load(R.drawable.ios_loading)
//                    .into(rootView.findViewById(R.id.gifImageView))
            }
            .build()
    }

    override fun showLoadingDialog(): Boolean {
        return LoadingPopup.showLoadingPopUp(loadingDialog)
    }

    override fun hideLoadingDialog(): Boolean {
        return LoadingPopup.hideLoadingPopUp(loadingDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController = Navigation.findNavController(this, R.id.fragment_main_container)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)
    }

    override fun logOut() {
        userDataManager.clearUserData()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun changeBottomNavigationState(isDefault: Boolean) {
        if(isDefault){
            binding.bottomNav.setBackgroundResource(R.drawable.shape_bottom_nav_default)
        } else {
            binding.bottomNav.setBackgroundResource(R.drawable.shape_bottom_nav)
        }
    }

}