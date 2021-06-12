package com.grank.datacenter.model

data class LoginResponse(
    var id: Int,
    var username: String,
    var password: String,
    var icon: String?,
    var type: Int,
    var collectIds: List<Int>?
)