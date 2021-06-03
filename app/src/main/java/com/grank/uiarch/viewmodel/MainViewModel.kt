package com.grank.uiarch.viewmodel

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.grank.uiarch.model.AppRepository
import com.grank.uicommon.ui.base.BaseViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ActivityRetainedScoped
class MainViewModel
@ViewModelInject
constructor(
    application: Application,
    private val dataStore: DataStore<Preferences>,
    private val repository: AppRepository
) : BaseViewModel(application) {

    private val key = preferencesKey<String>("first_key")
    private val _text = MediatorLiveData<String>().apply {
        value = "this main view model"
    }
    val text = Transformations.map(_text) {
        it
    }
    val url = MutableLiveData<String>().apply {
        value = "https://www.baidu.com/img/pc_77ae6a71fb2655cc1cc4ea1c7e7c41b6.gif"
    }
     fun getvv()
    {
        viewModelScope.launch {
            dataStore.edit {
                it[key] = "aa"
            }
        }
    }
    fun geta()  = dataStore.data.map {
        it[key]
    }
}