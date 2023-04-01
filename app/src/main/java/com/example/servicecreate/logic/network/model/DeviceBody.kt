package com.example.servicecreate.logic.network.model

/**
 * @author:SunShibo
 * @date:2023-04-01 11:44
 * @feature:
 */
data class DeviceKindListResponse(
    val code: Int,
    val `data`: List<DeviceKindData>,
    val map: Map,
    val msg: Any
)

data class DeviceKindData(
    val avatar: Any,
    val id: Int,
    val name: String
)
