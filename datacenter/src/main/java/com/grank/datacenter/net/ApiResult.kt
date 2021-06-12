package com.grank.datacenter.net

import androidx.annotation.IntDef
import com.grank.logger.Log
import com.grank.datacenter.net.ApiResult.FAIL_TYPE.Companion.EC_ERROR
import com.grank.datacenter.net.ApiResult.FAIL_TYPE.Companion.HTTP_ERROR
import com.grank.datacenter.net.ApiResult.FAIL_TYPE.Companion.RC_ERROR
import retrofit2.Response
import java.net.HttpURLConnection

/**
 * 这个类是用于将后台返回的json串，进行一次封装，
 * 此类目的是：通过解析里面ret返回值，来告诉使用方知道此次调用成功与失败，这样就要求后台返回
 * 的值有固定的格式 eg:
 * {
 *   "ret":1,后台返回结果
 *   "errorMsg":"错误描述信息",
 *   "data":{T的泛型结构}
 * }
 * 由于这个项目只是一个架构代码，没有与相关业务进行结合处理，所以这块先全部以response.isSuccessful
 * 判断即响应成功。由相应的业务再自己去解析ret并做相应的处理
 *
 * 后续如果结合了相关业务，可以在这里做一些调整
 *
 * @param T
 */
abstract class ApiResult<T> {

    abstract fun needRetry(): Boolean
    abstract fun log()

    companion object {

        internal fun <T> parse(
            response: Response<CommonResponse<T>>,
            hasRealData: Boolean
        ): ApiResult<T>? {
            if (!response.isSuccessful) {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                val errorMessage = "status code :  ${response.code()} \n error message: ${
                    if (!errorMsg.isNullOrEmpty()) errorMsg else "unknown error"
                }"
                return Fail(HTTP_ERROR, errorMessage, response.code())
            }
            val commonResponse: CommonResponse<T> =
                response.body() ?: return Fail(
                    HTTP_ERROR,
                    "empty response body error ",
                    HttpURLConnection.HTTP_NO_CONTENT,
                )
            val errorNumber = commonResponse.errorCode
            if (errorNumber != 0) {
                val errorMessage =
                    "RC error code : ${commonResponse.errorCode} and parsed code is $errorNumber, error msg: ${commonResponse.errorMsg}"
                return Fail(RC_ERROR, errorMessage, errorNumber,  commonResponse)
            }


            return Success(commonResponse)
        }

        fun <T> create(t: Throwable): ApiResult<T> {
            //没有网络时 传入UnknownHostException 是由SpDns抛出的
            return Fail(HTTP_ERROR, "meet exception: \n${t.stackTraceToString()}")
        }
    }

    class Success<T>(private val commonResponse: CommonResponse<T>) : ApiResult<T>() {

        fun getCommonResponse(): CommonResponse<T>? {
            return commonResponse
        }

        fun getRealData(): T {
            return commonResponse.data
        }

        override fun needRetry(): Boolean {
            return false
        }

        override fun log() {
            Log.v("OkHttp2", "ApiResult Success (data :\n $commonResponse \n)")
        }

    }

    @Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(HTTP_ERROR, RC_ERROR, EC_ERROR)
    annotation class FAIL_TYPE {

        companion object {

            /**
             *  Http status code 显示错误
             */
            const val HTTP_ERROR = 1

            /**
             *  后台网关错误
             */
            const val RC_ERROR = 2

            /**
             * 后台业务错误
             */
            const val EC_ERROR = 3
        }

    }

    class Fail<T>(
        val failType: @FAIL_TYPE Int,
        val errorMessage: String,
        val errorNumber: Int = 0, // 和 FailType 对应的具体错误号，错误码表查看ErrorCode
        private val commonResponse: CommonResponse<T>? = null
    ) : ApiResult<T>() {

        fun getCommonResponse(): CommonResponse<T>? {
            return commonResponse
        }

        fun getRealData(): T? {
            return commonResponse?.data
        }

        override fun needRetry(): Boolean {
            when (failType) {
                HTTP_ERROR -> {
                    return errorNumber !in 400..499
                }
                EC_ERROR, RC_ERROR -> {
                    return true
                }
            }
            return false
        }

        override fun toString(): String {
            val sb = StringBuilder()
            sb.append("Api Result Fail(")
            sb.append("failType=").append(failType).append(",")
            sb.append("errorNumber=").append(errorNumber).append(",")
            sb.append("errorMessage=").append(errorMessage).append("")
            sb.append(")")
            return sb.toString()
        }

        override fun log() {
            val sb = StringBuilder()
            sb.append("ApiResult Fail(")
            sb.append("failType=").append(failType).append(",")
            sb.append(")\n")
            sb.append(errorMessage)
            Log.e("OkHttp2", sb.toString())
        }


    }
}