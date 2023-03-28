package com.example.uitoolkit.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.bumptech.glide.Glide
import com.example.uitoolkit.R
import com.example.uitoolkit.custom.models.ItemModel
import java.math.BigDecimal
import java.math.RoundingMode

class ProductView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var productViewModel = ItemModel()
    private var favouriteIcon: ImageView
    private var image: ImageView
    private var percentText: TextView
    private var mainLayout: ConstraintLayout
    private var lp: LayoutParams
    private var src: Int? = null
    var favouriteIconClick: ((Boolean) -> Unit)? = null


    init {
        val root = LayoutInflater.from(context).inflate(R.layout.product_view, this, true)
        favouriteIcon = root.findViewById(R.id.favouriteIcon)
        image = root.findViewById(R.id.mainImage)
        percentText = root.findViewById(R.id.percentText)
        mainLayout = root.findViewById(R.id.mainLayout)
        lp = mainLayout.layoutParams as LayoutParams
        loadAttr(attrs, defStyleAttr)
    }

    private fun loadAttr(attrs: AttributeSet?, defStyleAttr: Int) {
        val arr = context.obtainStyledAttributes(
            attrs,
            R.styleable.ProductView,
            defStyleAttr,
            0
        ).apply {
            productViewModel.percent =
                getString(R.styleable.ProductView_percent)
            productViewModel.favouriteIconVisibility =
                getBoolean(
                    R.styleable.ProductView_favourite_icon_visibility, false
                )
            productViewModel.favouriteIconSelected =
                getBoolean(R.styleable.ProductView_favourite_icon_selected, false)
        }

        arr.recycle()
        refreshViewState()

    }

    private fun refreshViewState() {
        productViewModel.percent?.let { percentText.text = it }
        productViewModel.imageurl?.let { setImageUrl(it) }
        src?.let { image.setImageDrawable(ContextCompat.getDrawable(context, it)) }

        if (productViewModel.favouriteIconVisibility) {
            favouriteIcon.visibility = View.VISIBLE
        } else {
            favouriteIcon.visibility = View.GONE
        }

        if (productViewModel.favouriteIconSelected) {
            favouriteIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.favourite_icon_selected
                )
            )
        } else {
            favouriteIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.favourite_icon_unselected
                )
            )
        }

        productViewModel.previousPrice?.let {
            getPrice(productViewModel.previousPrice!!,productViewModel.currentPrice!!)
        }

        favouriteIcon.setOnClickListener { favouriteIconClick?.invoke(productViewModel.favouriteIconSelected) }
    }

    fun setViewData(data: ItemModel) {
        productViewModel = data
        refreshViewState()
    }

    fun getPercentText() : TextView {

        return percentText
    }

    private fun setImageUrl(url: String) {
        Glide.with(context).load(url).into(image)
    }

    private fun getPrice(previous: BigDecimal, current: BigDecimal) {
        val percent = (current.multiply(100.toBigDecimal())).divide(previous,0, RoundingMode.HALF_UP)
        val result = 100.toBigDecimal().subtract(percent)
        percentText.visibility = View.VISIBLE
        percentText.text = "-$result%"
        Log.d("TAG", "getPrice: " + result)
    }

}