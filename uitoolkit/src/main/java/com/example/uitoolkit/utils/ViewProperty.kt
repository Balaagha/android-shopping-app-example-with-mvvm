package com.example.uitoolkit.utils

import android.graphics.drawable.Drawable
import android.view.View
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * An extension for properties on View classes to initialize with [ViewPropertyDelegate].
 *
 * @param defaultValue A default value for this property.
 *
 * @return A [ViewPropertyDelegate] which is readable and writable property.
 */
@JvmSynthetic
internal fun <T : Any?> View.viewProperty(defaultValue: T): ViewPropertyDelegate<T> {
    return ViewPropertyDelegate(defaultValue) {
        invalidate()
    }
}

@JvmSynthetic
internal fun <T : Any?> View.viewPropertyWidth(defaultValue: T): ViewPropertyDelegate<T> {
    return ViewPropertyDelegate(defaultValue) { propertyValue ->
        val layoutParams = this.layoutParams
        (propertyValue as? Int)?.let { layoutParams.width = it }
    }
}

fun <T : Any?> View.viewPropertyHeight(defaultValue: T): ViewPropertyDelegate<T> {
    return ViewPropertyDelegate(defaultValue) { propertyValue ->
        val layoutParams = this.layoutParams
        (propertyValue as? Int)?.let { layoutParams.height = it }
    }
}

@JvmSynthetic
internal fun <T : Any?> View.viewPropertyBgRes(defaultValue: T): ViewPropertyDelegate<T> {
    return ViewPropertyDelegate(defaultValue) { propertyValue ->
        (propertyValue as? Int)?.let { this.setBackgroundResource(it) }
    }
}

@JvmSynthetic
internal fun <T : Any?> View.viewPropertyBg(defaultValue: T): ViewPropertyDelegate<T> {
    return ViewPropertyDelegate(defaultValue) { propertyValue ->
        (propertyValue as? Drawable)?.let { this.background = it }
    }
}




/**
 * A delegate class to invalidate View class if the [propertyValue] has been updated by a new value.
 *
 * @param defaultValue A default value for this property.
 * @param invalidator An executable lambda to invalidate [View].
 *
 * @return A readable and writable property.
 */
class ViewPropertyDelegate<T : Any?>(
    defaultValue: T,
    private val invalidator: (value: T) -> Unit
) : ReadWriteProperty<Any?, T> {

    private var propertyValue: T = defaultValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return propertyValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if (propertyValue != value) {
            propertyValue = value
            invalidator(value)
        }
    }
}
