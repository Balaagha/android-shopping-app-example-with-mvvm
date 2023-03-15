package com.example.androidmvvmcleanarchitectureexample.ui

import android.os.Bundle
import com.example.androidmvvmcleanarchitectureexample.databinding.ActivityMainBinding
import com.example.core.helper.viewInflateBinding
import com.example.core.view.BaseActivity
import com.example.uitoolkit.loading.LoadingPopup
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by viewInflateBinding(ActivityMainBinding::inflate)

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
    }

}