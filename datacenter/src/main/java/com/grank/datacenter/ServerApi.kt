package com.grank.datacenter

import com.grank.datacenter.net.ApiResult
import com.grank.datacenter.model.*
import retrofit2.http.Body
import retrofit2.http.POST

    val SERVER = "https://localhost:6878/"
//val SERVER = "http://139.198.189.139:8080/"
//https://customer-dev-api.fcb.com.cn/backapi/appupdate/appapi/appupdate/v1/application/newversion
//val SERVER = "https://customer-dev-api.fcb.com.cn/"
/**
 * 1 在每个请求方法中，如果需要添加参数，则调用时传入的参数不能为null否则报错
 *   java.lang.IllegalArgumentException: Body parameter value must not be null
 * 2
 */
interface ServerApi {
    @POST("api/state")
    suspend fun getState(@Body req: GetStateReq=GetStateReq(1)): ApiResult<State>

    @POST("backapi/appupdate/appapi/appupdate/v1/application/newversion")
    suspend fun checkNewVersion(@Body req:GetNewVersionReq):ApiResult<Data>

    @POST("backapi/gethomepage")
    suspend fun gethomepage():ApiResult<TopPage>
}