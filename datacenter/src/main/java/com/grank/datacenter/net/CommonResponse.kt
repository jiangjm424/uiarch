package com.grank.datacenter.net

import com.google.gson.annotations.SerializedName

data class CommonResponse<T>(
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("errorMsg") val errorMsg: String,
    @SerializedName("data") val data: T)
