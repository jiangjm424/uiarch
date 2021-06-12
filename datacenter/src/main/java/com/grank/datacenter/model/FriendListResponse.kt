package com.grank.datacenter.model

    data class FriendListResponse(
        var id: Int,
        var name: String,
        var link: String,
        var visible: Int,
        var order: Int,
        var icon: Any
    )