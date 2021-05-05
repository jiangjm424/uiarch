package com.grank.uiarch.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.grank.uiarch.R
import com.grank.uiarch.testvo.DemoItemImageText

class DashboardViewModel : ViewModel() {

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