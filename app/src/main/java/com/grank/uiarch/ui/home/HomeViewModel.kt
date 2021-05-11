package com.grank.uiarch.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.grank.netcore.core.Resource
import com.grank.netcore.model.GetNewVersionResp
import com.grank.netcore.model.State
import com.grank.uiarch.model.AppRepository
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class HomeViewModel
@ViewModelInject constructor(
        private val appRepository: AppRepository
) : ViewModel() {

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

    private val _newAppVersion = MediatorLiveData<Resource<GetNewVersionResp>>()
    val newAppVersion = Transformations.map(_newAppVersion) {
        it
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
}