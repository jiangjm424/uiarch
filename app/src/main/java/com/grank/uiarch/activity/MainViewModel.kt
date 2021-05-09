package com.grank.uiarch.activity

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.grank.uiarch.model.AppRepository
import com.grank.uiarch.ui.base.BaseViewModel
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
}