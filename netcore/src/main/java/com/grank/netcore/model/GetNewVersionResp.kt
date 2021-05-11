package com.grank.netcore.model

import java.util.*


/**
 * Auto-generated: 2021-05-10 8:51:43
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
class GetNewVersionResp {
    var code: String? = null
    var message: String? = null
    var data: Data? = null
}
data class Data (
    var versionId:Int = 0,
    var terminalType: String? = null,
    var versionNumber: String? = null,
    var isForce:Int = 0,
    var installUrl: String? = null,
    var updateExplain: String? = null,
    var compatibleVersion: List<String>? = null,
)