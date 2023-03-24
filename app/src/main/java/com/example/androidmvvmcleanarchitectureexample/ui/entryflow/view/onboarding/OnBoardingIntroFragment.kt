package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.onboarding

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentOnboardingIntroBinding
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.core.view.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OnBoardingIntroFragment : BaseMvvmFragment<FragmentOnboardingIntroBinding, EntryViewModel>(
    R.layout.fragment_onboarding_intro, EntryViewModel::class
) {

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_entry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnGoToOnBoardingSliderScreen.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingIntroFragment_to_onBoardingFragment)
        }
    }

}