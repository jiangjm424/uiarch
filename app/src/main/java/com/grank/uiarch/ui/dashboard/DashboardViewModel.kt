package com.grank.uiarch.ui.dashboard

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grank.uiarch.R
import com.grank.uiarch.model.AppRepository
import com.grank.uiarch.testvo.DemoItemImageText
import com.grank.uicommon.ui.base.BaseViewModel
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class DashboardViewModel
@ViewModelInject constructor(
    application: Application,
    private val dataStore: DataStore<Preferences>,
    private val appRepository: AppRepository
) : BaseViewModel(application)  {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    val item: LiveData<List<DemoItemImageText>>
        get() = this._items
    private val _items = MutableLiveData<List<DemoItemImageText>>().apply { 
        value = arrayListOf(
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
            DemoItemImageText(R.drawable.ic_home_black_24dp,"now time:${System.currentTimeMillis()}"),
        )
    }
}