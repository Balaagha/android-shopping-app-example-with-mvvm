package com.example.androidmvvmcleanarchitectureexample.ui.cardflow.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmvvmcleanarchitectureexample.R
import com.example.androidmvvmcleanarchitectureexample.databinding.FragmentCardsBinding
import com.example.androidmvvmcleanarchitectureexample.databinding.GenericRvCardItemBinding
import com.example.androidmvvmcleanarchitectureexample.ui.MainActivity
import com.example.androidmvvmcleanarchitectureexample.ui.cardflow.viewmodel.CardsViewModel
import com.example.common.adapters.genericadapter.GenericAdapter
import com.example.common.utils.extentions.getIfExists
import com.example.core.view.BaseMvvmFragment
import com.example.data.features.common.model.orders.ProductWrapper
import com.example.uitoolkit.custom.models.ItemModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CardsFragment :
    BaseMvvmFragment<FragmentCardsBinding, CardsViewModel>(
        R.layout.fragment_cards, CardsViewModel::class
    ) {

    override val viewModelFactoryOwner: () -> ViewModelStoreOwner = {
        findNavController().getViewModelStoreOwner(R.id.nav_graph_cart)
    }

    private val mCardItemAdapter by lazy {
        GenericAdapter<ProductWrapper>(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClickListener()
        initProductAdapter()
        initViewObserver()
    }

    override fun performOnViewCreatedOnlyOnce() {
        viewModel.getCart()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewObserver() {
        Log.d("CardsFragment", "initViewObserver: called")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("CardsFragment", "in viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)")
                viewModel.productUiData.collect {
                    Log.d("CardsFragment", "initViewObserver: it?.size => ${it?.size} | it => $it for productUiData")
                    if(it != null) {
                        mCardItemAdapter.setData(
                            list = it,
                            notifyFunc = { mAdapter ->
                                mAdapter.notifyDataSetChanged()
                            }
                        )
                        binding.tvTotalPrice.setViewText("US $${viewModel.calculateTotalPrice()}")
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("CardsFragment", "in viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED)")
                viewModel.selectedProductIdsData.collect {
                    Log.d("CardsFragment", "initViewObserver: it?.size => ${it?.size} | it => $it for selectedProductIdsData")
                    if((it?.size ?: 0) > 0) {
                        binding.checkoutWrapper.isVisible = true
                        binding.tvTotalPrice.setViewText("US $${viewModel.calculateTotalPrice()}")
                        binding.btnDeleteIcon.isEnabled = true
                        binding.btnDeleteIcon.iconTint = AppCompatResources.getColorStateList(requireContext(),R.color.colorAccentCarroot)
                        (activity as? MainActivity)?.changeBottomNavigationState(true)
                    } else {
                        binding.checkoutWrapper.isVisible = false
                        binding.btnDeleteIcon.isEnabled = false
                        binding.btnDeleteIcon.iconTint = AppCompatResources.getColorStateList(requireContext(),R.color.colorGray20)
                        (activity as? MainActivity)?.changeBottomNavigationState(false)
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        with(binding) {
            btnDeleteIcon.setOnClickListener {
                viewModel.deleteSelectedProducts()
            }
            btnCheckout.setOnClickListener {
                viewModel.updateCardForPlaceOrder()
            }
            navigationBackBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initProductAdapter() {
        mCardItemAdapter.expressionViewHolderBinding =
            { item, viewType, isAlreadyRendered, viewBinding ->
                val itemView = viewBinding as GenericRvCardItemBinding
                with(itemView) {
                    title.setViewText(item.product?.name)
                    subTitle.setViewText("US $${item.cartQuantity * (item.product?.currentPrice ?: 1)}")
                    val productModel = ItemModel(
                        imageurl = item.product?.imageUrls?.getIfExists(0)
                    )
                    productView.setViewData(productModel)
                    btnMinus.setOnClickListener {
                        viewModel.decreaseQuantity(item)
                        viewModel.updateCards()
                    }
                    btnPlus.setOnClickListener {
                        viewModel.increaseQuantity(item)
                        viewModel.updateCards()
                    }
                    btnIsSelectCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                        Log.d("CardsFragment", "initProductAdapter: isChecked => $isChecked")
                        item.product?.id?.let {
                            viewModel.updateSelectedProductData(it, isChecked)
                        }
                    }
                    tvCount.setViewText(item.cartQuantity.toString())
                }
            }
        mCardItemAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
            GenericRvCardItemBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        }
        mCardItemAdapter.expressionOnGetItemViewType = { _, position ->
            position
        }
        val manager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        with(binding.rvProductList) {
            layoutManager = manager
            adapter = mCardItemAdapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? MainActivity)?.changeBottomNavigationState(false)
    }

}