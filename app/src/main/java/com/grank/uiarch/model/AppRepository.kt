package com.grank.uiarch.model

import androidx.lifecycle.liveData
import com.grank.datacenter.MainDb
import com.grank.datacenter.ServerApi
import com.grank.datacenter.db.DemoEntity
import com.grank.datacenter.net.ApiResult
import com.grank.datacenter.net.Resource
import com.grank.datacenter.model.GetNewVersionReq
import com.grank.datacenter.model.GetNewVersionResp
import com.grank.datacenter.model.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        private val serverApi: ServerApi,
        private val mainDb: MainDb
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
    fun checkNewVersion() = liveData<Resource<GetNewVersionResp>> {
        emit(Resource.loading())   //开始请求网络时，这里将请求状态置加载中，这样在UI中可以根据这个值来显示加载中的UI
        val result = serverApi.checkNewVersion(GetNewVersionReq().apply {
            applicationPackage = "com.fcb.bao.consumer"
            terminalType = "1"
            versionNumber = "1.3.3"
            uuid = "177f0db7ce997474"
        })
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

    fun addDemoItem(demoEntity: DemoEntity) = flow<Int> {
        mainDb.getDemoDao().insert(demoEntity)
        emit(1)
    }.flowOn(Dispatchers.IO)
    fun all() = mainDb.getDemoDao().reportItems

}