package com.grank.uiarch.model

import androidx.lifecycle.liveData
import com.grank.datacenter.MainDb
import com.grank.datacenter.ServerApi
import com.grank.datacenter.net.ApiResult
import com.grank.datacenter.net.Resource
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
class AppRepository @Inject constructor(
    private val serverApi: ServerApi,
    private val mainDb: MainDb
) {
    fun getHomeList(page: Int) = liveData {
        emit(Resource.loading())
        val result = serverApi.getHomeList(page)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getTypeTreeList() = liveData {
        emit(Resource.loading())
        val result = serverApi.getTypeTreeList()
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getArticleList(page: Int, cid: Int) = liveData {
        emit(Resource.loading())
        val result = serverApi.getArticleList(page, cid)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getFriendList() = liveData {
        emit(Resource.loading())
        val result = serverApi.getFriendList()
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getHotKeyList() = liveData {
        emit(Resource.loading())
        val result = serverApi.getHotKeyList()
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getSearchList(page: Int, k: String) = liveData {
        emit(Resource.loading())
        val result = serverApi.getSearchList(page, k)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun loginWanAndroid(userName: String, pwd: String) = liveData {
        emit(Resource.loading())
        val result = serverApi.loginWanAndroid(userName, pwd)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun registerWanAndroid(userName: String, pwd: String, pwdCheck: String) = liveData {
        emit(Resource.loading())
        val result = serverApi.registerWanAndroid(userName, pwd, pwdCheck)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getLikeList(page: Int) = liveData {
        emit(Resource.loading())
        val result = serverApi.getLikeList(page)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun addCollectArticle(id: Int) = liveData {
        emit(Resource.loading())
        val result = serverApi.addCollectArticle(id)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun addCollectOutsideArticle(title: String, author: String, link: String) = liveData {
        emit(Resource.loading())
        val result = serverApi.addCollectOutsideArticle(title, author, link)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun removeCollectArticle(id: Int, originalId: Int = -1) = liveData {
        emit(Resource.loading())
        val result = serverApi.removeCollectArticle(id, originalId)
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getBanner() = liveData {
        emit(Resource.loading())
        val result = serverApi.getBanner()
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }

    fun getBookmarkList() = liveData {
        emit(Resource.loading())
        val result = serverApi.getBookmarkList()
        result.log()
        when (result) {
            is ApiResult.Success -> {
                emit(Resource.success(result.getRealData()))
            }
            is ApiResult.Fail -> {
                emit(Resource.fail(result.errorNumber, result.errorMessage, result.getRealData()))
            }
        }
    }
}