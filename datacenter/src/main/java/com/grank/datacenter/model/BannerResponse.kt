package com.grank.datacenter.model

data class BannerResponse(
    var id: Int,
    var url: String,
    var imagePath: String,
    var title: String,
    var desc: String,
    var isVisible: Int,
    var order: Int,
    var `type`: Int
)