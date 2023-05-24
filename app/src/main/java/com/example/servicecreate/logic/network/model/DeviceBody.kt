package com.example.servicecreate.logic.network.model

/**
 * @author:SunShibo
 * @date:2023-04-01 11:44
 * @feature:
 */
data class DeviceListResponse(
    val code: Int,
    val `data`: List<DeviceData>,
    val map: Map,
    val msg: String?
)

data class DeviceData(
    val createTime: String,
    val id: Long,
    val isDeleted: Int,
    val name: String,
    val roomList: List<RoomData>,
    val type: Int,
    val state: Boolean,
    val updateTime: String
)

data class DeviceKindData(
    val avatar: Any,
    val id: Int,
    val name: String
)
data class BraceletResponse(
    val code: Int,
    val `data`: BraceData,
    val map: Map,
    val msg: Any
)

data class BraceData(
    val heart: Int,
    val higeper: Int,
    val id: Int,
    val lowper: Int,
    val steps: Int
)

data class AddDeviceToRoomRequest(
    val id: String,
    val name: String,
    val roomIdList: List<String>,
    val type: Int
)

data class ChangeDeviceRoomRequest(
    val equipmentId: String,
    val roomIdList: List<String>
)


