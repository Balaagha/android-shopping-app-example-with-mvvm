package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentSplashBinding
import com.example.androidmvvmcleanarchitectureexample.ui.MainActivity
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.core.view.BaseMvvmFragment
import com.example.data.helper.manager.UserDataManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SplashFragment : BaseMvvmFragment<FragmentSplashBinding, EntryViewModel>(
    R.layout.fragment_splash, EntryViewModel::class
) {

    @Inject
    lateinit var userDataManager: UserDataManager

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_entry)
    }

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }

    private val runner by lazy {
        Runnable {
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                if (userDataManager.setApiToken()) {
                    activity?.apply {
                        finish()
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_onBoardingIntroFragment)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler.postDelayed(runner, SPLASH_SCREEN_TIME)
    }


    companion object {
        const val SPLASH_SCREEN_TIME = 1500L
    }
}