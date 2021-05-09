package com.grank.netcore

import com.grank.netcore.core.ApiResult
import com.grank.netcore.model.GetStateReq
import com.grank.netcore.model.GetStateResp
import com.grank.netcore.model.State
import retrofit2.http.Body
import retrofit2.http.POST

//    val SERVER = "https://localhost:6878/"
val SERVER = "http://139.198.189.139:8080/"

/**
 * 1 在每个请求方法中，如果需要添加参数，则调用时传入的参数不能为null否则报错
 *   java.lang.IllegalArgumentException: Body parameter value must not be null
 * 2
 */
interface ServerApi {
    @POST("api/state")
    suspend fun getState(@Body req: GetStateReq=GetStateReq(1)): ApiResult<State>
}