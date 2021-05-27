package com.grank.datacenter.model


/**
 * Auto-generated: 2021-05-10 8:51:43
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
class TopPage {
    var cardsData:List<ViewCard> = emptyList()
}
data class ViewCard(val cardId:Int, val title:String, val cardType:Int, val data: Map<String,String>)