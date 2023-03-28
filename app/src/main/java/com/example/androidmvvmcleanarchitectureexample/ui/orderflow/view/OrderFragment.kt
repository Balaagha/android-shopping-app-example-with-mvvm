package com.example.androidmvvmcleanarchitectureexample.ui.orderflow.view

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentOrderBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.GenericRvOrderItemBinding
import com.example.androidmvvmcleanarchitectureexample.ui.orderflow.model.ProductItemUiModel
import com.example.androidmvvmcleanarchitectureexample.ui.orderflow.viewmodel.OrderViewModel
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.core.view.BaseMvvmFragment
import com.example.uitoolkit.custom.models.ItemModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderFragment :
    BaseMvvmFragment<FragmentOrderBinding, OrderViewModel>(
        R.layout.fragment_order, OrderViewModel::class
    ) {

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_orders)
    }

    private val mCanceledAdapter by lazy {
        GenericAdapter<ProductItemUiModel>(requireContext())
    }
    private val mOngoingAdapter by lazy {
        GenericAdapter<ProductItemUiModel>(requireContext())
    }
    private val mCompletedAdapter by lazy {
        GenericAdapter<ProductItemUiModel>(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        initCanceledAdapter()
        initViewObserver()
    }

    override fun performOnViewCreatedOnlyOnce() {
        viewModel.getUserOrdersData()
    }

    private fun initViewObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onGoingOrderUiData.collect {
                    if ((it?.size ?: 0) > 0) {
                        Log.d("AddProductFragment", "viewModel.onGoingOrderUiData: is collected with data => $it  | size => ${it?.size}")
                        mOngoingAdapter.setData(
                            list = it,
                            notifyFunc = { mAdapter ->
                                mAdapter.notifyDataSetChanged()
                            }
                        )
                    }
                }
            }
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.canceledOrderUiData.collect {
                    if ((it?.size ?: 0) > 0) {
                        Log.d("AddProductFragment", "viewModel.canceledOrderUiData: is collected with data => $it  | size => ${it?.size}")
                        mCanceledAdapter.setData(
                            list = it,
                            notifyFunc = { mAdapter ->
                                mAdapter.notifyDataSetChanged()
                            }
                        )
                    }
                }
            }
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.competedOrderUiData.collect {
                    if ((it?.size ?: 0) > 0) {
                        Log.d("AddProductFragment", "viewModel.competedOrderUiData: is collected with data => $it  | size => ${it?.size}")
                        mCompletedAdapter.setData(
                            list = it,
                            notifyFunc = { mAdapter ->
                                mAdapter.notifyDataSetChanged()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        with(binding) {
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            expandableOngoingView.setOnClickListener {
                expand(
                    targetView = if((viewModel.onGoingOrderUiData.value?.size ?: 0) > 0) binding.rvOngoingList else binding.emptyListOngoing,
                )
            }
            expandableCanceledView.setOnClickListener {
                expand(
                    targetView = if((viewModel.canceledOrderUiData.value?.size ?: 0) > 0) binding.rvCanceledList else binding.emptyListCanceled,
                )
            }
            expandableComplatedView.setOnClickListener {
                expand(
                    targetView = if((viewModel.competedOrderUiData.value?.size ?: 0) > 0) binding.rvCompletedList else binding.emptyListCompleted,
                )
            }
        }
    }

    private fun initCanceledAdapter() {
        mOngoingAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as GenericRvOrderItemBinding
                with(itemView) {
                    title.setViewText(item.title)
                    subTitle.setViewText(item.subTitle)
                    tvStatusTextView.setViewText(item.statusText)
                    val productModel = ItemModel(
                        imageurl = item.imageUrl
                    )
                    productView.setViewData(productModel)

                }
            }
        mOngoingAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            GenericRvOrderItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        }
        mOngoingAdapter.expressionOnGetItemViewType = { _, position ->
            position
        }
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        with(binding.rvOngoingList) {
            layoutManager = manager
            adapter = mOngoingAdapter
        }

    }

    private fun expand(isExpand: Boolean? = null, targetView: ViewGroup ) {
        binding.apply {
            TransitionManager.beginDelayedTransition(pageWrapper, AutoTransition())
            isExpand?.let {
                targetView.isVisible = isExpand
            } ?: run {
                targetView.isVisible = targetView.isVisible.not()
            }
        }
    }

}