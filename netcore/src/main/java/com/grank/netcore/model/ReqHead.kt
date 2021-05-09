package com.grank.netcore.model

import android.os.Build
import com.google.gson.annotations.SerializedName

data class ReqHead
constructor(
    @SerializedName("androidId") val androidId: String?="androidId",              //终端androidId
    @SerializedName("imei") val imei: String?="imei",                              //终端imei
    @SerializedName("imsi") val imsi: String?="imsi",                              //终端imsi
    @SerializedName("mac") val mac: String?="mac",                                 //终端mac
    @SerializedName("manufacture") val manufacture: String = Build.MANUFACTURER,                //生产商
    @SerializedName("mode") val mode: String = Build.MODEL,                                     //型号
    @SerializedName("userId") val userId: String? = "",                                         //用户id
    @SerializedName("ticketId") val ticketId: String? = "",                                     //ticket id
    @SerializedName("appName") val appName: String="app name",          //APP name
    @SerializedName("appVersionCode") val appVersionCode: Int=1,       //APP version code
    @SerializedName("appVersionName") val appVersionName: String="1",    //APP version name
    @SerializedName("romVersion") val romVersion: String?=null,           //rom version
    @SerializedName("seq") val seq: Int = 0,                                                    //序列号
    @SerializedName("timestamp") val timestamp: Long? = 0,                                      //时间戳
    @SerializedName("deviceKey") val deviceKey: String? = ""                                    //设备签名
)