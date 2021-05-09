package com.grank.netcore.core

import android.net.NetworkInfo
import com.flipkart.okhttpstats.handler.OnResponseListener
import com.flipkart.okhttpstats.model.RequestStats
import com.grank.logger.Log


internal class NetTrafficStat : OnResponseListener {

    override fun onResponseSuccess(info: NetworkInfo, requestStats: RequestStats) {
        Log.v(
            "OkHttp", "onResponseSuccessReceived : "
                    + " Id : " + requestStats.id
                    + " Url : " + requestStats.url
                    + " Method : " + requestStats.methodType
                    + " Host : " + requestStats.hostName
                    + " Request Size : " + requestStats.requestSize + " Bytes"
                    + " Response Size : " + requestStats.responseSize + " Bytes"
                    + " Time Taken: " + (requestStats.endTime - requestStats.startTime) + " ms"
                    + " Status Code : " + requestStats.statusCode
        )
    }

    override fun onResponseError(info: NetworkInfo?, requestStats: RequestStats, e: Exception) {
        Log.v(
            "OkHttp", "onResponseErrorReceived : "
                    + " Id : " + requestStats.id
                    + " Url : " + requestStats.url
                    + " Method : " + requestStats.methodType
                    + " Host : " + requestStats.hostName
                    + " Request Size : " + requestStats.requestSize + " Bytes"
                    + " Response Size : " + requestStats.responseSize + " Bytes"
                    + " Time Taken: " + (requestStats.endTime - requestStats.startTime) + " ms"
                    + " Status Code : " + requestStats.statusCode
                    + " Exception : " + e.message
        )
    }
}

