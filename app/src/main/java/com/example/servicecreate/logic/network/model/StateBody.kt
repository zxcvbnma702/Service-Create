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

