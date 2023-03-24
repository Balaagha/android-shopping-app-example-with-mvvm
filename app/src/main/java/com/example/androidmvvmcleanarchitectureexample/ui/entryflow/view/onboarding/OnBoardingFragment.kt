package com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.onboarding

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentOnBoardingBinding
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.onboarding.screens.OnBoardingFirstFragment
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.onboarding.screens.OnBoardingSecondFragment
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.view.onboarding.screens.OnBoardingThreeFragment
import com.example.androidmvvmcleanarchitectureexample.ui.entryflow.viewmodel.EntryViewModel
import com.example.core.view.BaseMvvmFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_on_boarding.*

@AndroidEntryPoint
class OnBoardingFragment : BaseMvvmFragment<FragmentOnBoardingBinding, EntryViewModel>(
    R.layout.fragment_on_boarding, EntryViewModel::class
) {

    private val fragmentList by lazy {
        arrayListOf<Fragment>(
            OnBoardingFirstFragment(),
            OnBoardingSecondFragment(),
            OnBoardingThreeFragment()
        )
    }

    private val adapter by lazy {
        OnBoardingViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

    }
    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_entry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initClickListener()
        setupIndicators(
            binding.indicatorContainer,
            adapter.itemCount,
            onBoardingViewPager.currentItem
        )
        Log.d("myTqag", "onViewCreated: ${onBoardingViewPager.currentItem}")
    }

    private fun initAdapter() {
        binding.onBoardingViewPager.adapter = adapter
        onBoardingViewPager.itemDecorationCount
        onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                when (position) {
                    0 -> {
                        btnContinue.text = "Next"
                    }
                    1 -> {
                        btnContinue.text = "Next"
                    }
                    2 -> {
                        btnContinue.text = "Get started"
                    }
                }
            }
        })
    }


    private fun initClickListener() {
        btnContinue.setOnClickListener {
            when (onBoardingViewPager.currentItem) {
                0 -> {
                    onBoardingViewPager.currentItem = 1
                }
                1 -> {
                    onBoardingViewPager.currentItem = 2
                }
                2 -> {
                    findNavController().navigate(R.id.action_onBoardingFragment_to_loginFlowTypeFragment)
                }
            }
        }
    }

    private fun setupIndicators(
        container: ViewGroup,
        itemCount: Int = 3,
        activeIndicatorIndex: Int = 0
    ) {
        val indicators: Array<ImageView?> = arrayOfNulls<ImageView>(itemCount)
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(6, 0, 6, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.apply {
                this.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        if (activeIndicatorIndex == i) R.drawable.bg_indicator_active else R.drawable.bg_indicator_in_active
                    )
                )
                this.layoutParams = layoutParams
                container.addView(this)
            }
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.bg_indicator_active,
                        null
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.bg_indicator_in_active,
                        null
                    )
                )
            }
        }
    }

}