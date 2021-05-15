package com.grank.uiarch.activity

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.grank.uiarch.model.AppRepository
import com.grank.uicommon.ui.base.BaseViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped

@ActivityRetainedScoped
class MainViewModel
@ViewModelInject
constructor(
    application: Application,
    private val repository: AppRepository
) : BaseViewModel(application) {

    private val _text = MediatorLiveData<String>().apply {
        value = "this main view model"
    }
    val text = Transformations.map(_text) {
        it
    }
    val url = MutableLiveData<String>().apply {
        value = "https://www.baidu.com/img/pc_77ae6a71fb2655cc1cc4ea1c7e7c41b6.gif"
    }
}