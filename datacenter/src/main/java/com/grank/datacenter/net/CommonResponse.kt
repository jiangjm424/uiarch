package com.grank.datacenter.net

import com.google.gson.annotations.SerializedName

data class CommonResponse<T>(
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T)
