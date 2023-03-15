package com.example.uitoolkit.helper

import android.text.*
import android.util.Log
import com.example.common.utils.extentions.isNotNull

import com.example.common.utils.extentions.isNull
import com.google.android.material.textfield.TextInputLayout


class InputTextWatcherDelegate(
    private val inputLayout: TextInputLayout,
    private val mask: String? = null,
    private val maskDecoderBlock: ((String) -> String)? = null,
    private val minTextLength: Int? = null,
    private val maxTextLength: Int? = null,
    deniedCharacters: ArrayList<String>? = null,
    private val isEnableTextCapCharacters: Boolean = false,
    private var errorText: String? = null,
    private val checkValidationWithCustomBehaviorBlock: ((String) -> Boolean)? = null,
    private val checkErrorVisibilityWithCustomBehaviorBlock: ((isValidValue: Boolean, errorTextValue: String?) -> Unit)? = null,
    private val beforeChangedBlock: (() -> Unit)? = null,
    private val onTextChangedBlock: (() -> Unit)? = null,
    private val afterTextChangedBlock: ((String) -> Unit)? = null,
    ) : TextWatcher {

    private var isRunning = false
    private var isDeleting = false

    var oldTextValue: String = ""

    private var isValid: Boolean = false

    init {
        if (minTextLength == 0 || minTextLength.isNull()) {
            isValid = true
        }
        deniedCharacters?.let { deniedCharacterList ->
            setDeniedCharacters(deniedCharacterList)
        }
    }

    fun getText(): String{
        return maskDecoderBlock?.invoke(oldTextValue) ?: oldTextValue
    }

    fun isValid(isValidAction: (()->Unit)? = null): Boolean{
        if(!isValid && (errorText.isNotNull() || checkValidationWithCustomBehaviorBlock.isNotNull())){
            checkErrorVisibilityWithCustomBehaviorBlock?.invoke(isValid, errorText) ?: run {
                inputLayout.error = errorText
            }
        }
        isValidAction?.let {
            if(isValid){
                isValidAction.invoke()
            }
        }
        return  isValid
    }

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        isDeleting = count > after
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        if (isRunning) { // if (isRunning || isDeleting) if you want it not working on deleting case, make like this
            return
        }

        isRunning = true

        if (maxTextLength.isNotNull() && (maskDecoderBlock?.invoke(editable.toString())?.length
                ?: editable.toString().length) > (maxTextLength ?: 0)
        ) {
            setTextViewValueWithoutNotifyWatcher(oldTextValue)
        } else {
            mask?.let { mMask ->
                val editableLength = editable.length
                if (editableLength < mMask.length) {
                    if (mMask[editableLength] != '#') {
                        editable.append(mMask[editableLength])
                    } else if (mMask[editableLength - 1] != '#') {
                        editable.insert(
                            editableLength - 1,
                            mMask,
                            editableLength - 1,
                            editableLength
                        )
                    }
                }
            }

            isValid =
                checkValidationWithCustomBehaviorBlock?.invoke(editable.toString()) ?: kotlin.run {
                    return@run checkValidation(maskDecoderBlock?.invoke(editable.toString()) ?: editable.toString())
                }

            checkErrorVisibilityWithCustomBehaviorBlock?.invoke(
                isValid, errorText
            ) ?: run {
                if(isValid){
                    checkErrorVisibility(isValidValue = isValid)
                }
            }

            oldTextValue = editable.toString()
            afterTextChangedBlock?.invoke(editable.toString())
        }
        isRunning = false
    }

    private fun setTextViewValueWithoutNotifyWatcher(
        value: String,
        cursorPosition: Int = value.length
    ) {
        inputLayout.apply {
            editText?.removeTextChangedListener(this@InputTextWatcherDelegate)
            val cursorPositionValue = when {
                cursorPosition < 0 -> 0
                cursorPosition > value.length -> value.length
                else -> cursorPosition
            }
            oldTextValue = try {
                editText?.setText(value)
                editText?.setSelection(cursorPositionValue)
                value
            } catch (e: Exception) {
                editText?.setText("")
                editText?.setSelection(0)
                ""
            }
            editText?.addTextChangedListener(this@InputTextWatcherDelegate)
        }
    }

    private fun setDeniedCharacters(deniedCharacterList: java.util.ArrayList<String>) {
        val filter: InputFilter =
            InputFilter { source, start, end, _, _, _ ->
                var keepOriginal = true
                val sb = StringBuilder(end - start)
                for (i in start until end) {
                    val c: Char? = source?.get(i)
                    if (!deniedCharacterList.contains(c.toString())) // put your condition here
                        sb.append(c) else keepOriginal = false
                }
                if (keepOriginal) null else {
                    if (source is Spanned) {
                        val sp = SpannableString(sb)
                        TextUtils.copySpansFrom(
                            source as Spanned?,
                            start,
                            sb.length,
                            null,
                            sp,
                            0
                        )
                        sp
                    } else {
                        sb
                    }
                }
            }
        inputLayout.editText?.filters = arrayOf(filter)
    }

    private fun checkValidation(value: String): Boolean {
        return value.length > (minTextLength ?: 0)
    }

    private fun checkErrorVisibility(
        isValidValue: Boolean = this.isValid,
        errorTextValue: String = this.errorText ?: ""
    ) {
        inputLayout.error = if (!isValidValue) {
            errorTextValue
        } else {
            null
        }
    }

}