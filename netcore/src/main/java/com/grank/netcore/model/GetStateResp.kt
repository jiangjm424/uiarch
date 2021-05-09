package com.grank.netcore.model

import com.google.gson.annotations.SerializedName

class GetStateResp {
    @SerializedName("total")
    var total = 0

    @SerializedName("stateList")
    var stateList: List<State>? = null
}