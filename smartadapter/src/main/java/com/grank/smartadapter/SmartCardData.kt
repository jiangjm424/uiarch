
package com.grank.smartadapter

/**
 *
 * 该类用于将同一数据类型，对应不同ViewCard显示模板做转换
 * 由于数据类型是一种，但是要求显示模板不同，可以将tag设置成对应的Adapter类名
 * @property data Any          保存的实体数据
 * @property tag String        在构建SmartCardData数据时，tag表示的是实际的卡片样式id
 * @property cardId String     本卡片的id,应用中同样样式的卡片可以有多个，这个cardId是用于区别不同的卡片
 * @property cardTitle String  本卡片的卡片名，可以没有。
 * @constructor
 */
/*
 * -----------------------------------------------------------------

 * -----------------------------------------------------------------
 */
data class SmartCardData(var data: Any, var tag: String,var cardId: String = "", var cardTitle:String = "")
