package com.grank.netcore.core

import com.google.gson.reflect.TypeToken
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResultCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawCallType = getRawType(callType)
        if (rawCallType != ApiResult::class.java) {
            throw IllegalArgumentException("param type must be api response")
        }

        if (callType is ParameterizedType) {
            val dataType = getParameterUpperBound(0, callType as ParameterizedType)
            val rawDataType = getRawType(dataType)
            val realDataSuperClass = rawDataType.genericSuperclass
            return ApiResultCallAdapter<Any>(dataType)

        }
        return ApiResultCallAdapter<Nothing>(Nothing::class.java)
    }

    abstract class CallDelegate<TIn, TOut>(
        protected val proxy: Call<TIn>
    ) : Call<TOut> {

        override fun execute(): Response<TOut> = throw NotImplementedError()

        final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
        final override fun clone(): Call<TOut> = cloneImpl()

        override fun cancel() = proxy.cancel()
        override fun request(): Request = proxy.request()
        override fun isExecuted() = proxy.isExecuted
        override fun isCanceled() = proxy.isCanceled
        //override fun timeout(): Timeout = proxy.timeout()

        abstract fun enqueueImpl(callback: Callback<TOut>)
        abstract fun cloneImpl(): Call<TOut>
    }

        class ResultCall<R>(proxy: Call<R>, val hasRealData: Boolean) :
        CallDelegate<R, ApiResult<R>>(proxy) {

        override fun enqueueImpl(callback: Callback<ApiResult<R>>) {
            proxy.enqueue(object : Callback<R> {

                override fun onResponse(call: Call<R>, response: Response<R>) {
                    val result = ApiResult.parse(response, hasRealData)
                    callback.onResponse(call as Call<ApiResult<R>>, Response.success(result))
                }

                override fun onFailure(call: Call<R>, t: Throwable) {
                    //没有网络时 会抛出UnknownHostException 是由SpDns抛出的
                    val result = ApiResult.create<Nothing>(t)
                    callback.onResponse(call as Call<ApiResult<R>>, Response.success(result) as Response<ApiResult<R>>)
                }

            })
        }

        override fun cloneImpl() = ResultCall(proxy.clone(), hasRealData)
    }

    class ApiResultCallAdapter<R>(private val dataType: Type) :
        CallAdapter<R, Call<ApiResult<R>>> {

        override fun responseType(): Type {
            return dataType //TypeToken.getParameterized(CommonResponse::class.java, dataType).type
        }

        override fun adapt(call: Call<R>): Call<ApiResult<R>> {
            return ResultCall(call, dataType != Nothing::class.java)
        }

    }
}
