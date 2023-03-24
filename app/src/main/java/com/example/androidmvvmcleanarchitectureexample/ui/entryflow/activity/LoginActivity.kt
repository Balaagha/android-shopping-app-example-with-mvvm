package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.ActivityLoginBinding
import com.example.core.helper.viewInflateBinding
import com.example.core.view.BaseActivity
import com.example.uitoolkit.loading.LoadingPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val binding: ActivityLoginBinding by viewInflateBinding(ActivityLoginBinding::inflate)

    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }


    private val loadingDialog: LoadingPopup? by lazy {
        LoadingPopup.getInstance(this).customLayoutLoading()
            .setCustomViewID(R.layout.dialog_custom_image_loading_popup,
                android.R.color.background_dark)
            .noIntentionalDelay()
            .setBackgroundOpacity(40)
            .cancelable(false)
            .setCustomizationBlock { context, rootView ->
                Glide.with(context).load(R.drawable.ios_loading)
                    .into(rootView.findViewById(R.id.gifImageView))
            }
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        navController.addOnDestinationChangedListener { _: NavController, nd: NavDestination, _: Bundle? ->
            val color: Int = when (nd.id) {
                R.id.splashFragment -> {
                    R.color.white
                }
                else -> R.color.white
            }
            setWindowDecoration(color)
        }
    }

    override fun showLoadingDialog(): Boolean {
        return LoadingPopup.showLoadingPopUp(loadingDialog)
    }

    override fun hideLoadingDialog(): Boolean {
        return LoadingPopup.hideLoadingPopUp(loadingDialog)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (isTaskRoot
            && supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.backStackEntryCount == 0
            && supportFragmentManager.backStackEntryCount == 0
        ) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (isTaskRoot && isFinishing) {
            finishAfterTransition()
        }
        super.onDestroy()
    }

}