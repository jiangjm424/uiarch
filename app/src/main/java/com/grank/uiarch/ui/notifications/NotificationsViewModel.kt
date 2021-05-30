package com.grank.uiarch.ui.notifications

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grank.uiarch.model.AppRepository
import com.grank.uicommon.ui.base.BaseViewModel
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class NotificationsViewModel
@ViewModelInject constructor(
    application: Application,
    private val dataStore: DataStore<Preferences>,
    private val appRepository: AppRepository
) : BaseViewModel(application)  {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}