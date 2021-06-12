package com.grank.uiarch.ui.home

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.grank.datacenter.model.Article
import com.grank.datacenter.model.HomeListResponse
import com.grank.datacenter.net.Resource
import com.grank.logger.Log
import com.grank.uiarch.model.AppRepository
import com.grank.uicommon.ui.base.BaseViewModel
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class HomeViewModel
@ViewModelInject constructor(
    application: Application,
    private val dataStore: DataStore<Preferences>,
    private val appRepository: AppRepository
) : BaseViewModel(application) {

    init {
        Log.i("jiang", "homview :$this")
    }

    private val _articles = MediatorLiveData<Resource<HomeListResponse>>()
    val articles = Transformations.map(_articles) {
        if (it.status == Resource.Status.SUCCESS)
            it.data!!.datas
        else
            emptyList<Article>()
    }
    private var currentPageNo = 0
    fun getHomeArticles() {
        currentPageNo = 0
        val source = appRepository.getHomeList(currentPageNo)
        _articles.addSource(source) {
            _articles.value = it
            if (it.status != Resource.Status.LOADING) {
                _articles.removeSource(source)
            }
        }
    }

    fun loadNextPage(): LiveData<Resource<List<Article>?>> {
        val nextPage = currentPageNo + 1
        return  Transformations.map(appRepository.getHomeList(nextPage)) {
            if (it.status == Resource.Status.FAIL) {
                Resource.fail(it.errorCode,it.message,null)
            } else if (it.status == Resource.Status.SUCCESS) {
                currentPageNo = nextPage
                Resource.success(it.data!!.datas!!)
            } else {
                Resource.fail(it.status,it.message,null)
            }
        }
    }
}