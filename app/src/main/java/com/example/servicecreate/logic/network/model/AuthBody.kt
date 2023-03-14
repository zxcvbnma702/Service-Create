package com.example.servicecreate.logic.network.model

/**
 * @author:SunShibo
 * @date:2023-03-13 0:00
 * @feature:
 */

data class SendVerifiedBody(
    val qq: String
)

data class SendVerifiedResponse(
    val code: Int,
    val `data`: String,
    val map: Map,
    val msg: String
)

class Map

data class LoginResponse(
    val code: Int,
    val `data`: Data,
    val map: Map,
    val msg: String
)

data class Data(
    val createTime: String,
    val id: String,
    val isDeleted: Int,
    val password: String,
    val phone: String,
    val qq: String,
    val status: Int,
    val updateTime: String
)