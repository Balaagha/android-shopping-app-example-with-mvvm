package com.example.core.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.utils.extentions.getMyString
import com.example.common.utils.extentions.observe
import com.example.core.R
import com.example.core.event.BaseUiEvent
import com.example.core.viewmodel.BaseViewModel
import com.example.uitoolkit.popup.GenericPopUpHelper
import kotlin.reflect.KClass

abstract class BaseMvvmFragment<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>,
) : BaseFragment<VB>(layoutId) {

    // Flags params
    private var isInitializedFirstTime: Boolean = false
    private var isViewDestroyed: Boolean = false

    /**
     * For nav graph scope -> override val viewModelFactoryOwner: () -> ViewModelStoreOwner = { findNavController().getViewModelStoreOwner(R.id.target_nav_graph_id) }
     * For activity scope  -> override val viewModelFactoryOwner: () -> ViewModelStoreOwner = { requireActivity() }
     */
    open val viewModelFactoryOwner: (() -> ViewModelStoreOwner) = { this }

    open val factoryProducer: ViewModelProvider.Factory by lazy { defaultViewModelProviderFactory }
    open val viewModel: VM by createViewModelLazy(
        viewModelClass,
        { viewModelFactoryOwner.invoke().viewModelStore },
        { factoryProducer }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInitialize(savedInstanceState)
        processNavigationRoute()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isViewDestroyed){
            performOnViewCreatedIfViewDestroyedAtLeastOnce()
            isViewDestroyed = false
        }
    }

    private fun initInitialize(savedInstanceState: Bundle?) {
        if (isInitializedFirstTime) return
        lifecycleScope.launchWhenStarted {
            performOnViewCreatedOnlyOnce()
        }
        initObservers()
        isInitializedFirstTime = true
    }

    private fun initObservers() {
        // Listen to events
        observe(viewModel.event, ::handleGenericsUiActionEvents)
        observe(viewModel.loadingEvent, ::handleLoadingIndicatorEvent)

        if(behaveAsDialog){
            observe(viewModel.eventUiActionForDialogFragment) {
                Log.d("myTagEvent", " call eventUiAction => $it | it?.first => ${it?.first}| this.javaClass => ${this.javaClass} | ${it?.first == this.javaClass}")
                if (it?.first == this.javaClass) {
                    handleUiActionEvent(it.second)
                }
            }
        } else {
            observe(viewModel.eventUiAction) {
                Log.d("myTagEvent", " call eventUiAction => $it | it?.first => ${it?.first}| this.javaClass => ${this.javaClass} | ${it?.first == this.javaClass}")
                if (it?.first == this.javaClass) {
                    handleUiActionEvent(it.second)
                }
            }
        }
    }

    protected open fun handleUiActionEvent(action: String?) {
        Log.d("myTagEvent", " call handleUiActionEvent => $action in super class")
        when (action) {
            "finish" -> {
                requireActivity().finish()
            }
            else -> {
                // NoImpl
            }
        }
    }

    protected open fun performOnViewCreatedOnlyOnce() {
        // NoImpl
    }

    protected open fun performOnViewCreatedIfViewDestroyedAtLeastOnce() {
        // NoImpl
    }

    protected open fun handleGenericsUiActionEvents(uiActionEvent: BaseUiEvent?) {
        Log.d("myTag", " call handleEvent => $uiActionEvent")
        when (uiActionEvent) {
            is BaseUiEvent.Alert -> {
                showAlertViaBaseUiEvent(uiActionEvent)
            }
            is BaseUiEvent.SnackBar -> {
                showSnackBarViaBaseUiEvent(uiActionEvent)
            }
            is BaseUiEvent.Toast -> {
                showToastViaBaseUiEvent(uiActionEvent)
            }
            is BaseUiEvent.LogOut -> {
                (activity as? BaseActivity)?.logOut()
            }
            else -> {
//                Timber.e("Unknown event handle $uiActionEvent ")
            }
        }
    }

    protected open fun showAlertViaBaseUiEvent(uiActionEvent: BaseUiEvent.Alert) {
        GenericPopUpHelper.Builder(childFragmentManager)
            .setStyle(GenericPopUpHelper.Style.STYLE_2_VERTICAL_BUTTONS)
            .setImage(R.drawable.ic_error_icon)
            .setImageLayoutParams(
                imageWith = 100,
                imageHeight = 100
            )
            .setTitle(uiActionEvent.title ?: getMyString(uiActionEvent.titleRes))
            .setTitleColor(ContextCompat.getColor(requireContext(), R.color.black))
            .setContent(uiActionEvent.message ?: getMyString(uiActionEvent.messageRes))
            .setContentColor(ContextCompat.getColor(requireContext(), R.color.black))
            .setPositiveButtonBackground(R.drawable.btn_approve)
            .setPositiveButtonTextAppearance(R.style.BoldBarlowBlack)
            .setPositiveButton("Okay") {
                it.dismiss()
            }
            .setNegativeButtonBackground(R.drawable.btn_cancel)
            .setNegativeButtonTextAppearance(R.style.BoldBarlowBlack)
            .setNegativeButton("Cancel", null)
            .create()
    }

    protected open fun showToastViaBaseUiEvent(uiActionEvent: BaseUiEvent.Toast) {
        Toast.makeText(
            requireContext(),
            "BaseUiEvent.Toast handle with title ${uiActionEvent.title.toString()}",
            Toast.LENGTH_SHORT
        ).show()
    }

    protected open fun showSnackBarViaBaseUiEvent(uiActionEvent: BaseUiEvent.SnackBar) {
        Toast.makeText(
            requireContext(),
            "BaseUiEvent.SnackBar handle with title ${uiActionEvent.title.toString()}",
            Toast.LENGTH_SHORT
        ).show()
    }

    protected open fun handleLoadingIndicatorEvent(isShow: Boolean?) {
        showLoadingIndicatorViaBaseUiEvent(isShow)
    }

    protected open fun showLoadingIndicatorViaBaseUiEvent(isShow: Boolean?) {
        if (isShow == true) {
            (activity as BaseActivity).showLoadingDialog()
        } else {
            (activity as BaseActivity).hideLoadingDialog()
        }
    }

    protected open fun processNavigationRoute(){
        observe(viewModel.navigationRoute) {
            if (it?.first == this.javaClass) {
                findNavController().navigate(it.second)
            }
        }
    }

    override fun onDestroy() {
        isInitializedFirstTime = false
        super.onDestroy()
    }

}