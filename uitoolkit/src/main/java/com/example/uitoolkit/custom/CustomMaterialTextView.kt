package com.example.uitoolkit.custom

import android.content.Context
import android.content.res.TypedArray
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.common.utils.extentions.isNotNull
import com.example.common.utils.extentions.isReferenceNotNull
import com.example.common.utils.extentions.setTextAppearanceAnyVersion
import com.example.uitoolkit.R
import com.example.uitoolkit.custom.CustomMaterialTextView.Companion.DEFAULT_VIEW_COLOR
import com.google.android.material.textview.MaterialTextView


/**
 * @constructor
 * Default Android view constructor arguments.
 *
 * @param context  The Context the AvatarView is running in, through which it can access the current theme, resources, etc.
 * @param attrs The attributes of the XML AvatarView tag that is inflating the view.
 * @param defStyleAttr The resource identifier of an attribute in the current theme whose value is the the resource id of a style.
 */
class CustomMaterialTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MaterialTextView(context, attrs, defStyleAttr) {

    companion object {
        const val EMPTY = ""

        @ColorInt
        const val DEFAULT_VIEW_COLOR = -16777216

        const val DEFAULT_LINE_SPACING = 0
        const val MULTIPLIER_LINE_SPACING = 1.0f
    }

    var textUiModel: CustomMaterialTextViewUIModel = CustomMaterialTextViewUIModel()

    init {
        loadAttr(context, attrs, defStyleAttr)
    }

    private fun loadAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomMaterialTextView,
            defStyleAttr,
            0
        ).apply {
            textUiModel.paddingHorizontal =
                getDimension(R.styleable.CustomMaterialTextView_horizontal_padding, 0F)
            textUiModel.paddingVertical =
                getDimension(R.styleable.CustomMaterialTextView_vertical_padding, 0F)
            textUiModel.text = getString(R.styleable.CustomMaterialTextView_text)
            textUiModel.textColorInt =
                getColor(R.styleable.CustomMaterialTextView_text_color, 0)
            textUiModel.textAppearance =
                getResourceId(
                    R.styleable.CustomMaterialTextView_text_appearance,
                    R.style.MRegularBlack
                )
            textUiModel.isSingleLine =
                getBoolean(R.styleable.CustomMaterialTextView_is_single_line, false)
            textUiModel.lineSpacingInt = getDimensionPixelSize(
                R.styleable.CustomMaterialTextView_line_spacing,
                DEFAULT_LINE_SPACING
            )
            textUiModel.backgroundDrawableRes = getResourceId(
                R.styleable.CustomMaterialTextView_background_res,
                0
            )
            textUiModel.drawablePaddingInt = getDimensionPixelSize(
                R.styleable.CustomMaterialTextView_drawable_padding,
                compoundDrawablePadding
            )
            textUiModel.drawableTintInt =
                getColor(R.styleable.CustomMaterialTextView_drawable_tint, DEFAULT_VIEW_COLOR)
            textUiModel.drawableStart =
                getDrawable(R.styleable.CustomMaterialTextView_drawable_start)
            textUiModel.drawableEnd = getDrawable(R.styleable.CustomMaterialTextView_drawable_end)
            textUiModel.drawableTop = getDrawable(R.styleable.CustomMaterialTextView_drawable_top)
            textUiModel.drawableBottom =
                getDrawable(R.styleable.CustomMaterialTextView_drawable_bottom)
            recycle()

            // Set attributes
            refreshViewState()
        }

    }

    private fun refreshViewState() {
        setViewBackground()
        setViewText()
        setViewTextAppearance()
        setViewTextColor()
        setViewSingleLineState()
        setViewLineSpacing()
        setViewVisibleState()
        setViewDrawablePadding()
        setViewDrawableTint()
        setViewDrawables()
        setViewPadding()

        this.textUiModel.maxLines?.let { this.maxLines = it }
        this.textUiModel.ellipsize?.let { this.ellipsize = it }
        this.textUiModel.gravity?.let { this.gravity = it }

    }


    /**
     * Set background of CustomMaterialTextView
     *
     * @param backgroundDrawableValue
     * @param backgroundResValue
     */
    fun setViewBackground(
        backgroundDrawableValue: Drawable? = null,
        @DrawableRes backgroundResValue: Int? = null
    ) {
        when {
            backgroundDrawableValue.isNotNull() -> {
                textUiModel.backgroundDrawable = backgroundDrawableValue
                textUiModel.backgroundDrawableRes = null
            }
            backgroundResValue.isReferenceNotNull() -> {
                textUiModel.backgroundDrawableRes = backgroundResValue
                textUiModel.backgroundDrawable = null
            }
        }

        textUiModel.backgroundDrawable?.let {
            this.background = it
        } ?: run {
            if (textUiModel.backgroundDrawableRes.isReferenceNotNull()) {
                this.setBackgroundResource(textUiModel.backgroundDrawableRes!!)
            }
        }
    }


    /**
     * Set text of CustomMaterialTextView
     *
     * @param textValue
     * @param charSequenceTextValue
     */
    fun setViewText(textValue: String? = null, charSequenceTextValue: CharSequence? = null) {
        if (textValue.isNotNull() || charSequenceTextValue.isNotNull()) {
            textUiModel.charSequenceText = charSequenceTextValue
            textUiModel.text = textValue
        }

        this.text = textUiModel.charSequenceText ?: textUiModel.text ?: EMPTY
    }

    /**
     * Set text color of CustomMaterialTextView
     *
     * @param resIdValue
     * @param colorIntValue
     */
    fun setViewTextColor(@ColorInt colorIntValue: Int? = null, @ColorRes resIdValue: Int? = null) {
        if (colorIntValue.isNotNull() || resIdValue.isNotNull()) {
            textUiModel.textColorInt = colorIntValue ?: resIdValue?.let { mResIdValue ->
                textUiModel.textColorRes = mResIdValue
                return@let ContextCompat.getColor(context, mResIdValue)
            }
        }

        if(textUiModel.textColorInt.isReferenceNotNull()) {
            setTextColor(textUiModel.textColorInt!!)
        }
    }

    /**
     * Set padding of CustomMaterialTextView
     *
     * @param paddingHorizontalValue
     * @param paddingHorizontalResValue
     * @param paddingVerticalValue
     * @param paddingVerticalResValue
     */
    fun setViewPadding(
        paddingHorizontalValue: Float? = null,
        @DimenRes paddingHorizontalResValue: Int? = null,
        paddingVerticalValue: Float? = null,
        @DimenRes paddingVerticalResValue: Int? = null
    ) {
        if (paddingHorizontalResValue.isNotNull() || paddingHorizontalValue.isNotNull()) {
            textUiModel.paddingHorizontal =
                paddingHorizontalValue ?: paddingHorizontalResValue?.let { mResIdValue ->
                    return@let context.resources.getDimension(mResIdValue)
                } ?: kotlin.run {
                    0f
                }
        }
        if (paddingVerticalValue.isNotNull() || paddingVerticalResValue.isNotNull()) {
            textUiModel.paddingVertical =
                paddingVerticalValue ?: paddingVerticalResValue?.let { mResIdValue ->
                    return@let context.resources.getDimension(mResIdValue)
                } ?: kotlin.run {
                    0f
                }
        }
        setPadding(
            textUiModel.paddingHorizontal.toInt(),
            textUiModel.paddingVertical.toInt(),
            textUiModel.paddingHorizontal.toInt(),
            textUiModel.paddingVertical.toInt()
        )
    }

    /**
     * Set text appearance of CustomMaterialTextView
     *
     * @param resIdValue
     */
    fun setViewTextAppearance(@StyleRes resIdValue: Int? = null) {
        resIdValue?.let {
            textUiModel.textAppearance = resIdValue
        }
        setTextAppearanceAnyVersion(textUiModel.textAppearance ?: R.style.MRegularBlack)
    }

    /**
     * Set single line CustomMaterialTextView
     *
     * @param singleLineStateValue
     */
    fun setViewSingleLineState(singleLineStateValue: Boolean? = null) {
        singleLineStateValue?.let {
            textUiModel.isSingleLine = it
        }
        this.isSingleLine = textUiModel.isSingleLine ?: false

        if (this.textUiModel.isSingleLine == true) {
            ellipsize = TextUtils.TruncateAt.END
        }
    }

    /**
     * Set visibility state of CustomMaterialTextView
     *
     * @param isVisibleStateValue
     */
    fun setViewVisibleState(
        isVisibleStateValue: Boolean? = null
    ) {
        if (isVisibleStateValue.isNotNull()) {
            isVisibleStateValue?.let {
                textUiModel.isVisible = it
            }
        }
        this.isVisible = textUiModel.isVisible ?: this.text.isNotEmpty()
    }


    /**
     * Set line spacing line CustomMaterialTextView
     *
     * @param lineSpacingValue
     * @param resIdValue
     */
    fun setViewLineSpacing(lineSpacingValue: Int? = null, @DimenRes resIdValue: Int? = null) {
        if (lineSpacingValue.isNotNull() || resIdValue.isNotNull()) {
            textUiModel.lineSpacingInt = lineSpacingValue ?: resIdValue?.let { mResIdValue ->
                textUiModel.lineSpacingRes = mResIdValue
                return@let context.resources.getDimensionPixelSize(mResIdValue)
            }
        }

        textUiModel.lineSpacingInt?.let { setLineSpacing(it.toFloat(), MULTIPLIER_LINE_SPACING) }
    }

    /**
     * Set drawable padding CustomMaterialTextView
     *
     * @param drawablePaddingIntValue
     * @param resIdValue
     */
    fun setViewDrawablePadding(
        drawablePaddingIntValue: Int? = null,
        @DimenRes resIdValue: Int? = null
    ) {
        if (drawablePaddingIntValue.isNotNull() || resIdValue.isNotNull()) {
            textUiModel.drawablePaddingInt =
                drawablePaddingIntValue ?: resIdValue?.let { mResIdValue ->
                    textUiModel.drawablePaddingRes = mResIdValue
                    return@let context.resources.getDimensionPixelSize(mResIdValue)
                }
        }
        textUiModel.drawablePaddingInt?.let {
            compoundDrawablePadding = it
        }
    }

    /**
     * Set drawables tint CustomMaterialTextView
     *
     * @param drawableTintIntValue
     * @param resIdValue
     */
    fun setViewDrawableTint(
        @ColorInt drawableTintIntValue: Int? = null,
        @ColorRes resIdValue: Int? = null
    ) {
        if (drawableTintIntValue.isNotNull() || resIdValue.isNotNull()) {
            textUiModel.drawableTintInt = drawableTintIntValue ?: resIdValue?.let { mResIdValue ->
                textUiModel.drawableTintRes = mResIdValue
                return@let ContextCompat.getColor(context, (mResIdValue))
            }
        }
        setDrawablesTint(textUiModel.drawableTintInt)
    }

    private fun setDrawablesTint(@ColorInt tint: Int?) {
        if (tint == null) return
        for (drawable in compoundDrawablesRelative) {
            if (drawable != null) {
                drawable.colorFilter = PorterDuffColorFilter(
                    tint,
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    /**
     * Set drawables of CustomMaterialTextView
     *
     * @param drawableStartValue
     * @param drawableStartResIdValue
     * @param drawableEndValue
     * @param drawableEndResIdValue
     * @param drawableTopValue
     * @param drawableTopResIdValue
     * @param drawableBottomValue
     * @param drawableBottomResIdValue
     */
    fun setViewDrawables(
        drawableStartValue: Drawable? = null,
        drawableEndValue: Drawable? = null,
        drawableTopValue: Drawable? = null,
        drawableBottomValue: Drawable? = null,
        @DrawableRes drawableStartResIdValue: Int? = null,
        @DrawableRes drawableEndResIdValue: Int? = null,
        @DrawableRes drawableTopResIdValue: Int? = null,
        @DrawableRes drawableBottomResIdValue: Int? = null,
    ) {
        if (drawableStartValue.isNotNull() || drawableStartResIdValue.isNotNull()) {
            textUiModel.drawableStart =
                drawableStartValue ?: drawableStartResIdValue?.let { mResIdValue ->
                    textUiModel.drawableStartRes = mResIdValue
                    return@let ContextCompat.getDrawable(context, (mResIdValue))
                }
        }
        if (drawableEndValue.isNotNull() || drawableEndResIdValue.isNotNull()) {
            textUiModel.drawableEnd =
                drawableEndValue ?: drawableEndResIdValue?.let { mResIdValue ->
                    textUiModel.drawableEndRes = mResIdValue
                    return@let ContextCompat.getDrawable(context, (mResIdValue))
                }
        }
        if (drawableTopValue.isNotNull() || drawableTopResIdValue.isNotNull()) {
            textUiModel.drawableTop =
                drawableTopValue ?: drawableTopResIdValue?.let { mResIdValue ->
                    textUiModel.drawableTopRes = mResIdValue
                    return@let ContextCompat.getDrawable(context, (mResIdValue))
                }
        }
        if (drawableBottomValue.isNotNull() || drawableBottomResIdValue.isNotNull()) {
            textUiModel.drawableBottom =
                drawableBottomValue ?: drawableBottomResIdValue?.let { mResIdValue ->
                    textUiModel.drawableBottomRes = mResIdValue
                    return@let ContextCompat.getDrawable(context, (mResIdValue))
                }
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            textUiModel.drawableStart,
            textUiModel.drawableTop,
            textUiModel.drawableEnd,
            textUiModel.drawableBottom
        )
    }


    fun setModel(model: CustomMaterialTextViewUIModel?, isRefreshView: Boolean = false) {
        if (model == null) return

        model.backgroundDrawableRes?.let { this.textUiModel.backgroundDrawableRes = it }
        model.backgroundDrawable?.let { this.textUiModel.backgroundDrawable = it }

        model.text?.let { this.textUiModel.text = it }
        model.charSequenceText?.let { this.textUiModel.charSequenceText = it }
        model.textColorInt?.let { this.textUiModel.textColorInt = it }
        model.textColorRes?.let {
            this.textUiModel.textColorInt = ContextCompat.getColor(context, it)
        }
        model.textAppearance?.let { this.textUiModel.textAppearance = it }
        model.isSingleLine?.let { this.textUiModel.isSingleLine = it }

        model.gravity?.let { this.textUiModel.gravity = it }
        model.maxLines?.let { this.textUiModel.maxLines = it }
        model.ellipsize?.let { this.textUiModel.ellipsize = it }


        model.lineSpacingInt?.let { this.textUiModel.lineSpacingInt = it }
        model.lineSpacingRes?.let { this.textUiModel.lineSpacingRes = it }

        model.isVisible?.let { this.textUiModel.isVisible = it }

        this.textUiModel.paddingHorizontal = model.paddingHorizontal
        this.textUiModel.paddingVertical = model.paddingVertical

        model.drawablePaddingInt?.let { this.textUiModel.drawablePaddingInt = it }
        model.drawablePaddingRes?.let { this.textUiModel.drawablePaddingRes = it }
        model.drawableTintInt?.let { this.textUiModel.drawableTintInt = it }
        model.drawableTintRes?.let { this.textUiModel.drawableTintRes = it }

        model.drawableStart?.let { this.textUiModel.drawableStart = it }
        model.drawableStartRes?.let { this.textUiModel.drawableStartRes = it }
        model.drawableEnd?.let { this.textUiModel.drawableEnd = it }
        model.drawableEndRes?.let { this.textUiModel.drawableEndRes = it }
        model.drawableTop?.let { this.textUiModel.drawableTop = it }
        model.drawableTopRes?.let { this.textUiModel.drawableTopRes = it }
        model.drawableBottom?.let { this.textUiModel.drawableBottom = it }
        model.drawableBottomRes?.let { this.textUiModel.drawableBottomRes = it }

        if (model.onClickListener != null) {
            setOnClickListener(model.onClickListener)
        }


        if (isRefreshView) {
            refreshViewState()
        }
    }

}


data class CustomMaterialTextViewUIModel(
    var text: String? = null,
    var charSequenceText: CharSequence? = null,

    var paddingHorizontal: Float = 0F,
    var paddingVertical: Float = 0F,

    @ColorInt
    var textColorInt: Int? = null,
    @ColorRes
    var textColorRes: Int? = null,

    var textAppearance: Int? = null,
    var isSingleLine: Boolean? = null,

    var lineSpacingInt: Int? = null,
    @DimenRes
    var lineSpacingRes: Int? = null,


    @DrawableRes var backgroundDrawableRes: Int? = null,
    var backgroundDrawable: Drawable? = null,

    var isVisible: Boolean? = null,

    var drawablePaddingInt: Int? = null,
    @DimenRes
    var drawablePaddingRes: Int? = null,

    @ColorInt
    var drawableTintInt: Int? = null,
    @ColorRes
    var drawableTintRes: Int? = null,

    var drawableStart: Drawable? = null,
    var drawableEnd: Drawable? = null,
    var drawableTop: Drawable? = null,
    var drawableBottom: Drawable? = null,

    @DrawableRes
    var drawableStartRes: Int? = null,
    @DrawableRes
    var drawableEndRes: Int? = null,
    @DrawableRes
    var drawableTopRes: Int? = null,
    @DrawableRes
    var drawableBottomRes: Int? = null,


    var onClickListener: View.OnClickListener? = null,
    var gravity: Int? = null, // Gravity.START
    var maxLines: Int? = null,
    var ellipsize: TextUtils.TruncateAt? = null

) {
    fun isVisibleViewByTextEmptyCase(): Boolean {
        return this.text?.isNotEmpty() == true || this.charSequenceText?.isNotEmpty() == true
    }
}


fun setCustomTextViewUiModelByAttr(
    typedArray: TypedArray,
    textView: CustomMaterialTextView,
    backgroundResAttr: Int? = null,
    textAttr: Int? = null,
    isVisibleStateAttr: Int? = null,
    textColorAttr: Int? = null,
    textAppearanceAttr: Int? = null,
    isSingleLineAttr: Int? = null,
    lineSpacingAttr: Int? = null,
    horizontalPaddingAttr: Int? = null,
    verticalPaddingAttr: Int? = null,
    drawablePaddingAttr: Int? = null,
    drawableTintIntAttr: Int? = null,
    drawableStartAttr: Int? = null,
    drawableEndAttr: Int? = null,
    drawableTopAttr: Int? = null,
    drawableBottomAttr: Int? = null
) {
    val uiModel = CustomMaterialTextViewUIModel().apply {

        this.backgroundDrawableRes = backgroundResAttr?.let { typedArray.getResourceId(it, 0) }

        this.text =  textAttr?.let { typedArray.getString(it) }

        this.isVisible = isVisibleStateAttr?.let {
            typedArray.getBoolean(
                it,
                this.isVisibleViewByTextEmptyCase()
            )
        } ?: run { this.isVisibleViewByTextEmptyCase() }

        this.textColorInt =
            textColorAttr?.let { typedArray.getColor(it, 0) }

        this.textAppearance =
            textAppearanceAttr?.let { typedArray.getResourceId(it, R.style.MRegularBlack) }

        this.isSingleLine =
            isSingleLineAttr?.let { typedArray.getBoolean(it, false) }
        this.lineSpacingInt =
            lineSpacingAttr?.let {
                typedArray.getDimensionPixelSize(
                    it,
                    CustomMaterialTextView.DEFAULT_LINE_SPACING
                )
            }

        this.paddingHorizontal =
            horizontalPaddingAttr?.let { typedArray.getDimension(it, 0F) } ?: run { 0F }
        this.paddingVertical =
            verticalPaddingAttr?.let { typedArray.getDimension(it, 0F) } ?: run { 0F }

        this.drawablePaddingInt =
            drawablePaddingAttr?.let {
                typedArray.getDimensionPixelSize(
                    it,
                    textView.compoundDrawablePadding
                )
            }
        this.drawableTintInt = drawableTintIntAttr?.let {
            typedArray.getColor(
                it,
                DEFAULT_VIEW_COLOR
            )
        }
        this.drawableStart = drawableStartAttr?.let { typedArray.getDrawable(it) }
        this.drawableEnd = drawableEndAttr?.let { typedArray.getDrawable(it) }
        this.drawableTop = drawableTopAttr?.let { typedArray.getDrawable(it) }
        this.drawableBottom = drawableBottomAttr?.let { typedArray.getDrawable(it) }
    }
    textView.setModel(
        model = uiModel,
        isRefreshView = false
    )
}