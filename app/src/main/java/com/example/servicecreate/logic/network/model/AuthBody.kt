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
    val msg: Any
)

class Map