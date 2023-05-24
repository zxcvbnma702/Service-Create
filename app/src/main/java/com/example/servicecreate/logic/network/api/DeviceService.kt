package com.example.servicecreate.logic.network.api

import com.example.servicecreate.logic.network.model.*
import retrofit2.Call
import retrofit2.http.*

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
    fun addDeviceToRoom(@Header("token") token: String, @Body requestBody: AddDeviceToRoomRequest):  Call<SendVerifiedResponse>

    @GET("equipment/find")
    fun findDevice(@Header("token") token: String): Call<DeviceListResponse>

    @GET("equipment/list")
    fun getDeviceList(@Header("token") token: String): Call<DeviceListResponse>

    @GET("equipment/list")
    fun getDeviceListByType(@Header("token") token: String, @Query("type") type: Int): Call<DeviceListResponse>

    @HTTP(method = "DELETE", path = "equipment", hasBody = true)
    fun deleteDevice(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

    @GET("equipment/conditioner")
    fun getAirState(@Header("token") token: String, @Query("id") id: Int): Call<AirDetailResponse>

    @GET("equipment/led")
    fun getLampState(@Header("token") token: String, @Query("id") id: Int): Call<LampDetailResponse>

    @GET("equipment/gate")
    fun getDoorLockState(@Header("token") token: String, @Query("id") id: Int): Call<DoorLockDetailResponse>

}