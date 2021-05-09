package com.grank.netcore.model

import android.os.Build
import com.google.gson.annotations.SerializedName

data class RespHead
constructor(
    @SerializedName("ret") val ret: Int,
    @SerializedName("version") private val version: String,
    @SerializedName("cmd") val cmd: String,
    @SerializedName("timeStamp") private val timeStamp: Long,
    @SerializedName("errorMsg") val errorMsg: String,
    @SerializedName("seq") val seq: Int
)