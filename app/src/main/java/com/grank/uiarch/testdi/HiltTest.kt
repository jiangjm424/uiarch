package com.grank.uiarch.testdi

import com.grank.logger.Log


class HiltTest {
    init {
        Log.i("jiang","Hilttest init $this")
    }
    fun print() = Log.i("jiang","hahahaha hilt")
}