package com.example.servicecreate.logic.network.model

/**
 * @author:SunShibo
 * @date:2023-03-30 11:26
 * @feature:
 */
data class RoomListResponse(
    val code: Int,
    val `data`: List<RoomData>,
    val map: Map,
    val msg: String?
)

data class RoomData(
    val createTime: String,
    val id: Long,
    val isDeleted: Int,
    val name: String,
    val updateTime: String,
    val equipmentNum: Int,
    val openLightNum: Int
)
