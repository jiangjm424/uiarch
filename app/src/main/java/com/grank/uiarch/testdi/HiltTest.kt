package com.grank.uiarch.testdi

import com.grank.logger.Log


class HiltTest {
    init {
        Log.i("jiang","Hilttest init $this")
    }
    fun log(m:String) {
        Log.i("jiang","log hilt my self")
    }
    fun print() = Log.i("jiang","hahahaha hilt")
}