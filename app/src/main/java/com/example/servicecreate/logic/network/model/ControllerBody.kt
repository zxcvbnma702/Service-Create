package com.example.servicecreate.logic.network.model
import com.google.gson.annotations.SerializedName


/**
 * @author:SunShibo
 * @date:2023-04-13 18:12
 * @feature:
 */
data class ScheduleBody(
    val date: String,
    val list: List<Item>,
    val name: String,
    val times: Int
)

data class Item(
    val equipmentId: String,
    val op: String
)

data class ScheduleTaskResponse(
    val code: Int,
    val `data`: List<TaskData>,
    val map: Map,
    val msg: Any
)

data class TaskData(
    val createTime: String,
    val deleteTime: String,
    val deleted: Boolean,
    val id: Long,
    val name: String,
    val publishUserId: Long,
    val updateTime: String
)


