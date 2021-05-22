package com.grank.datacenter.net

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.flipkart.okhttpstats.NetworkInterceptor
import com.flipkart.okhttpstats.handler.PersistentStatsHandler
import com.flipkart.okhttpstats.interpreter.DefaultInterpreter
import com.flipkart.okhttpstats.reporter.NetworkEventReporterImpl
import com.grank.logger.Log
import com.grank.datacenter.BuildConfig.*
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class ApiFactory(
    private val mContext: Context,
    private val vendorPlatform: VendorPlatform,
    apiServerUrl: String,
    allowUnsafeSsl: Boolean = false
) {
    companion object {
        private const val MAX_CACHE_SIZE = 5 * 1024 * 1024L
        private const val MAX_DAYS_OFFLINE_CACHE_STALE = 2 * 7 //离线时缓存有效期时间 以天为单位
        private const val RESPONSE_CACHE_VALID_MAX_AGE = 5 // 在线时缓存有效时间 以秒为单位

        private val MEDIA_TYPE = "application/json; charset=UTF-8".toMediaTypeOrNull()
    }
    var accountInfo: AccountInfo? = null

    @VisibleForTesting
    fun <T> create(service: Class<T>): T = retrofit.create(service)

    private val unsafeTrustAllCerts = arrayOf<X509TrustManager>(
        object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf<X509Certificate>()
            }

        }
    )

    private fun createUnsafeSocketFactory(): SSLSocketFactory {
        // Create a trust manager that does not validate certificate chains
        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, unsafeTrustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.socketFactory
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient().newBuilder().apply {
            configOkHttpNetAnalyzeHelper(this)
            configHttpRequestHeader(this)
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(10, TimeUnit.SECONDS)
            if (allowUnsafeSsl) {
                sslSocketFactory(createUnsafeSocketFactory(), unsafeTrustAllCerts[0])
                hostnameVerifier { hostname, session -> true }
            }
        }.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(apiServerUrl)
            .addCallAdapterFactory(ApiResultCallAdapterFactory())
//            .addConverterFactory(ApiRequestDataConverterFactory(Gson()))
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    private fun configHttpRequestHeader(okHttpClientBuilder: OkHttpClient.Builder) {
        okHttpClientBuilder.addInterceptor(object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val originReq = chain.request()
                return chain.proceed(originReq)
            }
        })
    }
    private fun configOkHttpNetAnalyzeHelper(okHttpClientBuilder: OkHttpClient.Builder) {
        if ((BUILD_TYPE == "proguard" || BUILD_TYPE == "debug")) {
            // 打印 连接开始 dns解析开始 结束 等各种事件，可用于定位请求耗时问题
            okHttpClientBuilder.eventListener(OkHttpEventListenerLogger("OkHttp"))
            okHttpClientBuilder.addNetworkInterceptor(HttpLoggingInterceptor(object :
                HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    when {
                        message.startsWith("{\"body") -> {
                            Log.json("OkHttp_RequestBody", message)
                        }
                        message.startsWith("{\"head") -> {
                            Log.json("OkHttp_ResponseBody", message)
                        }
                        else -> {
                            Log.v("OkHttp", message)
                        }
                    }
                }
            }).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        } else {

            //listener
            val networkRequestStatsHandler = PersistentStatsHandler(mContext)
            val netTrafficStat = NetTrafficStat()
            //register your listener with the PersistentStatsHandler
            networkRequestStatsHandler.addListener(netTrafficStat)
            val networkInterpreter =
                DefaultInterpreter(NetworkEventReporterImpl(networkRequestStatsHandler))
            val networkReporter = NetworkInterceptor.Builder()
                .setNetworkInterpreter(networkInterpreter)
                .setEnabled(true)
                .build()
            okHttpClientBuilder.addNetworkInterceptor(networkReporter)
        }
    }


}