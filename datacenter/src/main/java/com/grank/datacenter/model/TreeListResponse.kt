package com.grank.datacenter.model

import java.io.Serializable

data class TreeListResponse(
    var id: Int,
    var name: String,
    var courseId: Int,
    var parentChapterId: Int,
    var order: Int,
    var visible: Int,
    var children: List<Children>?
) : Serializable {
    data class Children(
        var id: Int,
        var name: String,
        var courseId: Int,
        var parentChapterId: Int,
        var order: Int,
        var visible: Int,
        var children: List<Children>?
    ) : Serializable
}