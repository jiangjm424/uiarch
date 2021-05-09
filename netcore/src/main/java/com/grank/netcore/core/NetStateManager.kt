package com.grank.netcore.core

import androidx.lifecycle.LiveData
import com.dlong.netstatus.DLNetManager
import com.dlong.netstatus.annotation.NetType

class NetStateManager(private val dlNetManager: DLNetManager) {
    val netStateLiveData:LiveData<@NetType String> = dlNetManager.getNetTypeLiveData()
}