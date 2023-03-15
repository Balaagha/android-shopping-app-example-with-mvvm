package com.example.data.base.usecase

import com.example.data.R
import com.example.data.base.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Response


abstract class BaseUseCaseForNetwork <ResponseType, in RequestParams> {

    internal abstract suspend fun run(params: RequestParams): DataWrapper<Response<ResponseType>>

    suspend operator fun invoke(params: RequestParams): DataWrapper<ResponseType> {
        return parseApiResponse(run(params))
    }

    open fun parseApiResponse(data: DataWrapper<Response<ResponseType>>): DataWrapper<ResponseType> {
        return when (data) {
            is DataWrapper.Success -> {
                when(val code = data.invoke()?.code()) {
                     in 200 .. 299 -> {
                        when (val resultBody = data.value.body()) {
                            null -> {
                                DataWrapper.Failure(
                                    failureType = FailureType.EMPTY_OR_NULL_RESULT,
                                    failureBehavior = FailureBehavior.SILENT,
                                    code = code
                                )
                            }
                            is List<*> -> {
                                if ((resultBody as List<*>).isNotEmpty()) {
                                    DataWrapper.Success(resultBody)
                                } else {
                                    DataWrapper.Failure(FailureType.EMPTY_OR_NULL_RESULT)
                                }
                            }
                            else -> {
                                DataWrapper.Success(resultBody)
                            }
                        }
                    }
                    in 300 .. 399 -> {
                        DataWrapper.Failure(
                            failureType = FailureType.API_REDIRECTION_CODE_RESPONSE,
                            code = data.value.code(),
                            message = data.value.errorBody()?.string(),
                            failureBehavior = FailureBehavior.SILENT
                        )
                    }
                    else -> {
                        if (code == 401) {
                            DataWrapper.Failure(
                                failureType = FailureType.AUTH_TOKEN_EXPIRED,
                                code = data.value.code(),
                                message = data.value.errorBody()?.string(),
                                failureBehavior = FailureBehavior.SILENT
                            )
                        } else {
                            val errorResponse: ApiErrorResponseModel? = parseErrorBody(data.value.errorBody())

                            DataWrapper.Failure(
                                failureType = FailureType.API_GENERIC_ERROR,
                                code = data.value.code(),
                                title = errorResponse?.error,
                                message = errorResponse?.errorDetails,
                                messageRes = if(errorResponse?.error == null && errorResponse?.errorDetails == null) R.string.base_generic_error else null,
                                failureBehavior = FailureBehavior.ALERT
                            )
                        }
                    }
                }

            }
            is DataWrapper.Failure -> {
                when (data.failureType) {
                    FailureType.NO_INTERNET_CONNECTION -> {
                        return DataWrapper.Failure(
                            failureType = FailureType.NO_INTERNET_CONNECTION,
                            failureBehavior = FailureBehavior.ALERT,
                            messageRes = R.string.base_no_internet_connection,
                        )
                    }
                    FailureType.RESPONSE_TIMEOUT -> {
                        return DataWrapper.Failure(
                            failureType = FailureType.RESPONSE_TIMEOUT,
                            failureBehavior = FailureBehavior.ALERT,
                            messageRes = R.string.base_operation_take_long_time
                        )
                    }
                    else -> DataWrapper.Failure(
                        failureType = data.failureType,
                        failureBehavior = FailureBehavior.ALERT,
                        messageRes = R.string.base_generic_error
                    )
                }
            }
            else -> {
                DataWrapper.Failure(
                    failureType = FailureType.OTHER,
                    failureBehavior = FailureBehavior.ALERT,
                    messageRes = R.string.base_generic_error
                )
            }
        }
    }

    open fun parseErrorBody(responseBody: ResponseBody?): ApiErrorResponseModel? {
        return responseBody?.let { errorResponseBody ->
            Gson().fromJson(
                errorResponseBody.charStream(),
                object : TypeToken<ApiErrorResponseModel>() {}.type
            )
        }
    }

}