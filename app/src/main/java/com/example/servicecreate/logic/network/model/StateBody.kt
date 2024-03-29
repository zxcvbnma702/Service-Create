package com.example.servicecreate.logic.network.model

/**
 * @author:SunShibo
 * @date:2023-04-07 23:52
 * @feature:
 */
data class AirDetailResponse(
    val code: Int,
    val `data`: AirStateData,
    val map: Map,
    val msg: Any
)

data class AirStateData(
    val createTime: String,
    val grade: Int,
    val id: Long,
    val is_deleted: Any,
    val mode: Int,
    val scaveng: Int,
    val state: Int,
    val temp: Int,
    val updateTime: String
)

data class LampDetailResponse(
    val code: Int,
    val `data`: LampStateData,
    val map: Map,
    val msg: Any
)

data class LampStateData(
    val createTime: String,
    val id: Long,
    val is_deleted: Any,
    val state: Int,
    val updateTime: String
)

data class DoorLockDetailResponse(
    val code: Int,
    val `data`: Data,
    val map: Map,
    val msg: Any
)

data class Data(
    val createTime: String,
    val id: Long,
    val isDeleted: Int,
    val password: String,
    val status: Int,
    val updateTime: String
)



