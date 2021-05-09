package com.grank.uiarch.model

import androidx.lifecycle.liveData
import com.grank.netcore.ServerApi
import com.grank.netcore.core.ApiResult
import com.grank.netcore.core.Resource
import com.grank.netcore.model.State
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AppRepository 是通过后台的api接口请求数据的管理类，这里在调用请求是会返回些次请求的状态
 * 包括：Resource.Status.
 *                  LOADING = 0
                    const val FAIL = 1
                    const val SUCCESS = 2
 * @property serverApi ServerApi
 * @constructor
 */
@Singleton
class AppRepository  @Inject constructor(
        private val serverApi: ServerApi
) {
    fun getState() = liveData<Resource<State>> {
        emit(Resource.loading())   //开始请求网络时，这里将请求状态置加载中，这样在UI中可以根据这个值来显示加载中的UI
        val result = serverApi.getState()
        result.log()
        when(result) {
            is ApiResult.Success -> {
                val dd = result.getRealData()
                emit(Resource.success(dd))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }
}