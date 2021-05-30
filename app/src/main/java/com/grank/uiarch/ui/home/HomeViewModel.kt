package com.grank.uiarch.ui.home

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.gson.Gson
import com.grank.datacenter.db.DemoEntity
import com.grank.datacenter.model.Data
import com.grank.datacenter.net.Resource
import com.grank.datacenter.model.State
import com.grank.datacenter.model.TopPage
import com.grank.logger.Log
import com.grank.smartadapter.SmartCardData
import com.grank.uiarch.model.AppRepository
import com.grank.uicommon.ui.base.BaseViewModel
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@FragmentScoped
class HomeViewModel
@ViewModelInject constructor(
    application: Application,
    private val dataStore: DataStore<Preferences>,
    private val appRepository: AppRepository
) : BaseViewModel(application) {

    init {
        Log.i("jiang","homview :$this")
    }
    //这里面监听数据根据实际情况，如果只要关注加载中，成功或者失败，则这里面监听的类型是Resource<State>
    //如果只是关心实际数据，则我们可以监听实际数据即可
    private val _state = MediatorLiveData<Resource<State>>()
    val text: LiveData<Resource<State>> = _state
    fun getState() {
        val source = appRepository.getState()
        _state.addSource(source) {
            _state.value = it
            if (it.status != Resource.Status.LOADING) {  //加载完成了，则这个数据源已经不用再次监听了
                _state.removeSource(source)
            }
        }
    }

    private val _newAppVersion = MediatorLiveData<Resource<Data>>()
    val newAppVersion = Transformations.map(_newAppVersion) {
        it
    }
    private val _toppage = MediatorLiveData<Resource<TopPage>>()
    val toppage = Transformations.map(_toppage) {
        if (Resource.Status.SUCCESS == it.status) {
            Resource.success(pageToCard(it.data))
        }else{
            Resource.fail(it.errorCode,it.message,null)
        }
    }
    private fun pageToCard(topPage: TopPage?):MutableList<SmartCardData> {
        val cards = mutableListOf<SmartCardData>()
        topPage?.let {
            it.cardsData.forEach { card->
                cards.add(SmartCardData(Gson().toJson(card.data), cardId = card.cardId.toString(), cardTitle = card.title,cardType = card.cardType.toString()))
            }
        }
        return cards
    }
    fun gettoppage() {
        val s = appRepository.gettoppage()
        _toppage.addSource(s) {
            _toppage.value = it
            if (it.status != Resource.Status.LOADING) {  //加载完成了，则这个数据源已经不用再次监听了
                _toppage.removeSource(s)
            }
        }

    }
    fun checkNewVersion() {
        val source = appRepository.checkNewVersion()
        _newAppVersion.addSource(source) {
            _newAppVersion.value = it
            if (it.status != Resource.Status.LOADING) {  //加载完成了，则这个数据源已经不用再次监听了
                _newAppVersion.removeSource(source)
            }
        }
    }

    fun add(demoEntity: DemoEntity) {
        viewModelScope.launch {
            appRepository.addDemoItem(demoEntity).collect {
                Log.i("jiang", "add item:$it")
            }
        }
    }

    fun getallDemo() = appRepository.all()
    fun fflow() = flow<Int> {
        emit(1)
    }
    val f = flowOf(1)

    private val key = preferencesKey<String>("first_key")
    fun getvv()
    {
        viewModelScope.launch {
            dataStore.edit {
                it[key] = "aa"
            }
        }
    }
    fun geta()  = dataStore.data.map {
        it.get(key)
    }
}