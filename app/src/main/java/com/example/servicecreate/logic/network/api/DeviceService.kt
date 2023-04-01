package com.example.servicecreate.logic.network.api

import com.example.servicecreate.logic.network.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * @author:SunShibo
 * @date:2023-04-01 11:43
 * @feature:
 */
interface DeviceService {

    @GET("equipment/kind")
    fun getDeviceKind(@Header("token") token: String): Call<RoomListResponse>

    @POST("equipment/new")
    fun addDevice(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

    @POST("equipment/add")
    fun addDeviceToRoom(@Header("token") token: String, @Body requestBody: Map<String, String>):  Call<SendVerifiedResponse>

}