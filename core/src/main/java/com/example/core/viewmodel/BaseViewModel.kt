package com.example.core.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.common.utils.helper.SingleLiveEvent
import com.example.core.event.BaseUiEvent
import com.example.data.base.models.DataWrapper
import com.example.data.base.models.FailureBehavior
import com.example.data.base.usecase.BaseUseCaseForNetwork
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BaseViewModel(
    val savedStateHandle: SavedStateHandle?,
    application: Application
) : AndroidViewModel(application) {

    /**
     * this field contains two type of data
     * first - class which if identify your current class is valid for navigation
     * second - root id which navigate your true destination
     * implementation in viewModel => navigationRouteId.postValue( Fragment::class.java to R.id.action_id )
     *
     */
    val navigationRoute = SingleLiveEvent<Pair<Class<*>, Int>>()

    open var isShowBaseLoadingIndicator: Boolean = false

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
//        Timber.e(throwable)
    }

    /**
     * For triggering base events on UI
     */
    val event: SingleLiveEvent<BaseUiEvent> =
        SingleLiveEvent()

    /**
     * For triggering base loading indicator
     */
    val loadingEvent: SingleLiveEvent<Boolean> =
        SingleLiveEvent()


    protected fun <T, R> BaseUseCaseForNetwork<T, R>.execute(
        params: R,
        successOperation: ((value: DataWrapper<T>) -> Unit)? = null,
        failOperation: ((value: DataWrapper<T>) -> Unit)? = null,
        block: ((value: DataWrapper<T>) -> Unit)? = null,
    ) {
        launchSafe {
                if(isShowBaseLoadingIndicator){
                    loadingEvent.postValue(true)
                }
                when (val result = this@execute.invoke(params)) {
                    is DataWrapper.Success -> {
                        if(isShowBaseLoadingIndicator){
                            loadingEvent.postValue(false)
                        }
                        successOperation?.invoke(result)
                    }
                    is DataWrapper.Failure -> {
                        if(isShowBaseLoadingIndicator){
                            loadingEvent.postValue(false)
                        }
                        processFailureExecution(result)
                        failOperation?.invoke(result)
                    }
                    else -> {
                        // Nothing
                    }
                }
        }
    }

    protected open fun <T> processFailureExecution(it: DataWrapper.Failure<T>) {
        when (it.failureBehavior) {
            FailureBehavior.ALERT -> {
                event.postValue(
                    BaseUiEvent.Alert(
                        title = it.title,
                        titleRes = it.titleRes,
                        message = it.message,
                        messageRes = it.messageRes
                    )
                )
            }
            FailureBehavior.SNACK_BAR -> {
                event.postValue(BaseUiEvent.SnackBar())
            }
            FailureBehavior.TOAST -> {
                event.postValue(BaseUiEvent.Toast())
            }
            else -> {
                // Nothing
            }
        }
    }

    protected fun launchSafe(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context + exceptionHandler, start, block)
    }

}