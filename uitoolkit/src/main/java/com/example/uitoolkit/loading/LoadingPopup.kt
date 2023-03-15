package com.example.uitoolkit.loading

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.uitoolkit.R

class LoadingPopup(context: Context) : Dialog(context) {

    private var intentionalDelayInMillSec: Long = 0

    @ColorRes
    var backgroundColor: Int = android.R.color.background_dark

    private var lottieAnimationPath = "Loading.json"

    @IntRange(from = 0, to = 100)
    private var opacity = DEFAULT_OPACITY

    private lateinit var vContent: View

    @LayoutRes
    private var customLayoutID = 0

    private var customLayoutBlock: ((context: Context, rootView: View) -> Unit)? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshViewState()
    }

    // Glide.with(context).load(R.drawable.ios_loading).into(imageView);

    // refresh view state region start
    private fun refreshViewState() {
        /** customLayoutID==0 means no custom layout is set. So it should show default layout. */
        if (customLayoutID == 0) {
            vContent = layoutInflater.inflate(R.layout.dialog_lottie_loading_popup, null)
            setupBackgroundColorWithOpacity()
            setContentView(vContent)
            val lottieAnimationView: LottieAnimationView = findViewById(R.id.v_lottie)
            lottieAnimationView.setAnimation(lottieAnimationPath)
        } else {
            vContent = layoutInflater.inflate(customLayoutID, null)
            setupBackgroundColorWithOpacity()
            setContentView(vContent)
        }

        customLayoutBlock?.invoke(context, vContent)
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            it.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
    }

    private fun setupBackgroundColorWithOpacity() {
        /**defaultBackgroundColor not black means user set a default color as watermark
         * background.If default color is black that means a default color is not set.
         * So then it should be get the background color from the inflated view from layout
         * resource id which will be shown as the water mark. If all of these approach get any exception
         * then black color with 50% opacity (default opacity) will be set*/
        if (backgroundColor == android.R.color.transparent)
            vContent.setBackgroundColor(Color.parseColor(ColorTransparentUtils
                .transparentColor(getBackgroundColor(vContent), opacity)))
        else {
            vContent.setBackgroundColor(Color.parseColor(ColorTransparentUtils
                .transparentColor(ContextCompat.getColor(context, backgroundColor), opacity)))
        }
    }

    private fun getBackgroundColor(view: View): Int {
        val drawable = view.background
        if (drawable is ColorDrawable) {
            return drawable.color
        }
        return Color.TRANSPARENT
    }
    // refresh view state region end

    override fun hide() {
        if (intentionalDelayInMillSec != 0L) {
            Handler(Looper.getMainLooper()).postDelayed({
                super.dismiss()
            }, intentionalDelayInMillSec)
        } else {
            super.dismiss()
        }
    }

    override fun show() {
        if (!isShowing) {
            super.show()
        }
    }


    private fun setCustomViewID(customLayoutID: Int, @ColorRes backgroundColor: Int, mLottieAnimationPath: String) {
        this.customLayoutID = customLayoutID
        this.backgroundColor = backgroundColor
        this.lottieAnimationPath = mLottieAnimationPath
    }



    private fun setCustomLayoutBlock(block: ((context: Context, rootView: View) -> Unit)?) {
        this.customLayoutBlock = block
    }

    private fun setIntentionalDelayInMillSec(millSec: Long) {
        intentionalDelayInMillSec = millSec
    }

    private fun setOpacity(opacity: Int) {
        this.opacity = opacity
    }

    /**
     * Sets whether this dialog is cancelable with the
     * {@link KeyEvent#KEYCODE_BACK BACK} key.
     */
    private fun setDialogCancelableState(isCancelable: Boolean) {
        setCancelable(isCancelable)
    }

    class LoadingBuilder(private val activity: Activity) : FinalStep, TypeStep, DelayDurationStep, DelayStep, CustomLayoutStep {

        private var dialog: LoadingPopup? = null

        private var isCanceledWithBackPress = false
        private var intentionalDelayInMillSecValue: Long = 0
        private var customLayoutBlockValue: ((context: Context, rootView: View) -> Unit)? = null


        @Synchronized
        fun getDialog(context: Context): LoadingPopup? {
            if (dialog == null) {
                dialog = LoadingPopup(context)
            }
            return dialog
        }

        @LayoutRes
        private var customLayoutIDValue = 0
        private var opacityValue = DEFAULT_OPACITY

        @ColorRes
        private var backgroundColorValue = android.R.color.transparent

        private var lottieAnimationPath = "Loading.json"

        // TypeStep region start
        override fun defaultLoading(): FinalStep {
            return this
        }

        override fun customLayoutLoading(): CustomLayoutStep {
            return this
        }
        // TypeStep region end

        // CustomLayoutStep region start
        override fun setCustomViewID(customLayoutID: Int): DelayStep {
            this.customLayoutIDValue = customLayoutID
            return this
        }

        override fun setCustomViewID(customLayoutID: Int, @ColorRes backgroundColor: Int): DelayStep {
            this.customLayoutIDValue = customLayoutID
            this.backgroundColorValue = backgroundColor
            return this
        }

        override fun setCustomLottieAnimation(
            animationAssetPath: String,
            backgroundColor: Int
        ): DelayStep {
            this.lottieAnimationPath = animationAssetPath
            this.backgroundColorValue = backgroundColor
            return this
        }

        override fun setCustomLottieAnimation(animationAssetPath: String): DelayStep {
            this.lottieAnimationPath = animationAssetPath
            return this
        }
        // CustomLayoutStep region end

        // DelayStep region start
        override fun doIntentionalDelay(): DelayDurationStep {
            return this
        }

        override fun noIntentionalDelay(): FinalStep {
            return this
        }

        override fun setDelayDurationInMillSec(millSec: Long): FinalStep {
            this.intentionalDelayInMillSecValue = millSec
            return this
        }
        // DelayStep region end

        // Final step region start
        override fun cancelable(isCancelable: Boolean): FinalStep {
            isCanceledWithBackPress = isCancelable
            return this
        }

        override fun setBackgroundOpacity(opacity: Int): FinalStep {
            this.opacityValue = opacity
            return this
        }

        override fun setBackgroundColor(colorId: Int): FinalStep {
            this.backgroundColorValue = colorId
            return this
        }

        override fun setCustomizationBlock(block: (context: Context, rootView: View) -> Unit): FinalStep {
            this.customLayoutBlockValue = block
            return this
        }
        // Final step region end


        override fun build(): LoadingPopup? {
            getDialog(activity)?.apply {
                setCustomViewID(this@LoadingBuilder.customLayoutIDValue, this@LoadingBuilder.backgroundColorValue,lottieAnimationPath)
                setCustomLayoutBlock(customLayoutBlockValue)
                setIntentionalDelayInMillSec(intentionalDelayInMillSecValue)
                setDialogCancelableState(isCanceledWithBackPress)
                setOpacity(opacityValue)
                refreshViewState()
            }
            return  dialog
        }

    }

    companion object {

        interface TypeStep {
            fun defaultLoading(): FinalStep
            fun customLayoutLoading(): CustomLayoutStep
        }

        interface CustomLayoutStep {
            fun setCustomViewID(@LayoutRes customLayoutID: Int): DelayStep
            fun setCustomViewID(@LayoutRes customLayoutID: Int, @ColorRes backgroundColor: Int): DelayStep
            fun setCustomLottieAnimation(animationAssetPath: String, @ColorRes backgroundColor: Int): DelayStep
            fun setCustomLottieAnimation(animationAssetPath: String): DelayStep
        }

        interface DelayStep {
            fun doIntentionalDelay(): DelayDurationStep
            fun noIntentionalDelay(): FinalStep
        }

        interface DelayDurationStep {
            fun setDelayDurationInMillSec(millSec: Long): FinalStep
        }

        interface FinalStep {
            fun cancelable(isCancelable: Boolean): FinalStep
            fun setBackgroundOpacity(@IntRange(from = 0, to = 100) opacity: Int): FinalStep
            fun setBackgroundColor(@ColorRes colorId: Int): FinalStep
            fun setCustomizationBlock(block: (context: Context,rootView: View) -> Unit): FinalStep
            fun build(): LoadingPopup?
        }

        @JvmStatic
        fun showLoadingPopUp(dialog: LoadingPopup?):Boolean {
            return try {
                dialog?.let {
                    it.show()
                    true
                } ?: run {
                    false
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in loading popup: " + e.message)
                false
            }
        }

        fun hideLoadingPopUp(dialog: LoadingPopup?): Boolean {
            dialog?.hide()
            return true
        }

        @JvmStatic
        @Synchronized
        fun getInstance(activity: Activity): TypeStep {
            return LoadingBuilder(activity)
        }


        private val TAG: String = LoadingPopup::class.java.simpleName
        private const val DEFAULT_OPACITY = 30
    }
}
