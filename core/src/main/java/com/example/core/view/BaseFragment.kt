package com.example.core.view

import android.app.Activity
import android.app.Dialog
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.core.helper.viewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson

abstract class BaseFragment<ViewDataBindingType : ViewDataBinding>(@LayoutRes val layoutId: Int) : DialogFragment() {

    /**
     * View data binding
     */
    open val binding: ViewDataBindingType by viewDataBinding()

    /**
     * Set if this should behave as bottom sheet
     */
    open val isBottomSheet: Boolean = false
    open val bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED
    open val isBottomSheetFullScreen = false
    open val isBottomSheetSkipCollapsed = false
    open val bottomSheetTopMargin = 120

    open val mIsCancelable: Boolean = true
    open val containerDialogView: View? = null

    /**
     * Determine if this fragment is registered as a dialog
     */
    open val behaveAsDialog: Boolean = false

    /**
     * Tracks if we are waiting for a dismissAllowingStateLoss or a regular dismiss once the
     * BottomSheet is hidden and onStateChanged() is called.
     */
    private var waitingForDismissAllowingStateLoss = false

    /**
     * onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showsDialog = behaveAsDialog
    }

    /**
     * onCreateDialog
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mDialog = if (isBottomSheet) CoreBottomSheetDialog(
            requireContext(),
            theme
        ) else super.onCreateDialog(
            savedInstanceState
        )
        if(isBottomSheet){
            (mDialog as? CoreBottomSheetDialog)?.behavior?.state = bottomSheetState
            (mDialog as? CoreBottomSheetDialog)?.behavior?.skipCollapsed = isBottomSheetSkipCollapsed
            if(isBottomSheetFullScreen){
                mDialog.setOnShowListener { dialogInterface ->
                    val coreBottomSheetDialog = dialogInterface as CoreBottomSheetDialog
                    coreBottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
                        setupFullHeight(this)
                    }
                }
            }
        }
        return mDialog
    }

    override fun onStart() {
        super.onStart()
        setupDialog()
    }

    private fun setupDialog(){
        if(behaveAsDialog){
            dialog?.window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                val backColor = ContextCompat.getColor(requireContext(), android.R.color.transparent)
                setBackgroundDrawable(ColorDrawable(backColor))
            }
            dialog?.setCancelable(mIsCancelable)
            if(mIsCancelable){
                containerDialogView?.setOnClickListener {
                    dismiss()
                }
            }
        }
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams

        val height: Int = Resources.getSystem().displayMetrics.heightPixels - bottomSheetTopMargin

        layoutParams.height = height
        bottomSheet.layoutParams = layoutParams
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    /**
     * This method is for hiding keyboard
     */
    fun hideKeyboard() {
        try {
            val inputMethodManager =
                context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
        } catch (e: Exception) {
//            Timber.e(e, "exception occurred at hide keyboard")
        }
    }

    /**
     * Dismiss dialog or bottom sheet dialog
     */
    override fun dismiss() {
        if (isBottomSheet) {
            if (!tryDismissWithAnimation(false)) {
                super.dismiss()
            }
        } else {
            super.dismiss()
        }
    }

    override fun dismissAllowingStateLoss() {
        if (isBottomSheet) {
            if (!tryDismissWithAnimation(true)) {
                super.dismissAllowingStateLoss()
            }
        } else {
            super.dismissAllowingStateLoss()
        }
    }

    private fun dismissWithAnimation(
        behavior: BottomSheetBehavior<*>, allowingStateLoss: Boolean
    ) {
        waitingForDismissAllowingStateLoss = allowingStateLoss
        if (behavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            dismissAfterAnimation()
        } else {
            dialog?.let {
                if (it is CoreBottomSheetDialog) it.removeDefaultCallback()
            }
            behavior.addBottomSheetCallback(BottomSheetDismissCallback(this))
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN)
        }
    }

    private fun dismissAfterAnimation() {
        if (waitingForDismissAllowingStateLoss) {
            super.dismissAllowingStateLoss()
        } else {
            super.dismiss()
        }
    }

    /**
     * Tries to dismiss the dialog fragment with the bottom sheet animation. Returns true if possible,
     * false otherwise.
     */
    private fun tryDismissWithAnimation(allowingStateLoss: Boolean): Boolean {
        val baseDialog = dialog
        if (baseDialog is BottomSheetDialog) {
            val dialog = baseDialog
            val behavior: BottomSheetBehavior<*> = dialog.behavior
            if (behavior.isHideable && dialog.dismissWithAnimation) {
                dismissWithAnimation(behavior, allowingStateLoss)
                return true
            }
        }
        return false
    }

    class BottomSheetDismissCallback(private val baseFragment: BaseFragment<*>) :
        BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                baseFragment.dismissAfterAnimation()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

}