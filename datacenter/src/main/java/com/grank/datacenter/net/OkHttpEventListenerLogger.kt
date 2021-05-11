package com.grank.datacenter.net

import androidx.annotation.Nullable
import com.grank.logger.Log
import okhttp3.*
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * Created by bruce on 2018/1/22.
 */

fun Call.getTag(): String {
    return request().url.lastPath()
}

fun HttpUrl.lastPath(): String {
    return encodedPath.split("/").last()
}

class OkHttpEventListenerLogger(private val tag: String) : EventListener() {
    override fun callStart(call: Call) {
        Log.v(tag + TAG + call.getTag(), "callStart $call")
    }

    override fun dnsStart(call: Call, domainName: String) {
        Log.v(tag + TAG + call.getTag(), "dnsStart $call, domainName $domainName")
    }

    override fun dnsEnd(call: Call, domainName: String, @Nullable inetAddressList: List<InetAddress>) {
        Log.v(tag + TAG + call.getTag(), "dnsEnd $call, domainName $domainName, inetAddressList $inetAddressList")
    }

    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        Log.v(tag + TAG + call.getTag(), "connectStart $call, inetSocketAddress $inetSocketAddress, proxy $proxy")
    }

    override fun secureConnectStart(call: Call) {
        Log.v(tag + TAG + call.getTag(), "secureConnectStart $call")
    }

    override fun secureConnectEnd(call: Call, @Nullable handshake: Handshake?) {
        Log.v(tag + TAG + call.getTag(), "secureConnectEnd $call, handshake $handshake")
    }

    override fun connectEnd(call: Call, inetSocketAddress: InetSocketAddress, @Nullable proxy: Proxy, @Nullable protocol: Protocol?) {
        Log.v(tag + TAG + call.getTag(), "connectEnd $call, inetSocketAddress $inetSocketAddress, proxy $proxy")
    }

    override fun connectFailed(call: Call, inetSocketAddress: InetSocketAddress, @Nullable proxy: Proxy, @Nullable protocol: Protocol?, @Nullable ioe: IOException) {
        Log.v(tag + TAG + call.getTag(), "connectFailed " + call + ", inetSocketAddress " + inetSocketAddress + ", proxy " + proxy + ", ioe " + ioe!!.message)
    }

    override fun connectionAcquired(call: Call, connection: Connection) {
        Log.v(tag + TAG + call.getTag(), "connectionAcquired $call, connection $connection")
    }

    override fun connectionReleased(call: Call, connection: Connection) {
        Log.v(tag + TAG + call.getTag(), "connectionReleased $call, connection $connection")
    }

    override fun requestHeadersStart(call: Call) {
        Log.v(tag + TAG + call.getTag(), "requestHeadersStart $call")
    }

    override fun requestHeadersEnd(call: Call, request: Request) {
        Log.v(tag + TAG + call.getTag(), "requestHeadersEnd $call, request $request")
    }

    override fun requestBodyStart(call: Call) {
        Log.v(tag + TAG + call.getTag(), "requestBodyStart $call")
    }

    override fun requestBodyEnd(call: Call, byteCount: Long) {
        Log.v(tag + TAG + call.getTag(), "requestBodyEnd $call")
    }

    override fun responseHeadersStart(call: Call) {
        Log.v(tag + TAG + call.getTag(), "responseHeadersStart $call")
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        Log.v(tag + TAG + call.getTag(), "responseHeadersEnd $call, response $response")
    }

    override fun responseBodyStart(call: Call) {
        Log.v(tag + TAG + call.getTag(), "responseBodyStart $call")
    }

    override fun responseBodyEnd(call: Call, byteCount: Long) {
        Log.v(tag + TAG + call.getTag(), "responseBodyEnd $call, byteCount $byteCount")
    }

    override fun callEnd(call: Call) {
        Log.v(tag + TAG + call.getTag(), "callEnd $call")
    }

    override fun callFailed(call: Call, ioe: IOException) {
        Log.v(tag + TAG + call.getTag(), "callFailed " + call + ", ioe " + ioe!!.message)
    }

    companion object {
        const val TAG = "-EL-"
    }
}

