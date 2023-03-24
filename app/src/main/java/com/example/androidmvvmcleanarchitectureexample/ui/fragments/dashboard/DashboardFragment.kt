package com.example.androidmvvmcleanarchitectureexample.ui.fragments.dashboard

import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentDashboardBinding
import com.example.core.view.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseMvvmFragment<FragmentDashboardBinding, DashboardViewModel>(
    R.layout.fragment_dashboard,
    DashboardViewModel::class
) {


}