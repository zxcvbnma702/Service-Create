package com.example.servicecreate.logic.network.api

import androidx.room.Delete
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.network.model.ChangeDeviceRoomRequest
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import retrofit2.Call
import retrofit2.http.*
import java.math.BigDecimal

/**
 * @author:SunShibo
 * @date:2023-03-27 23:26
 * @feature:
 */
interface RoomService {

    @GET("room/list")
    fun getRoomList(@Header("token") token: String): Call<RoomListResponse>

    @POST("room/new")
    fun addRoom(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

    @HTTP(method = "DELETE", path = "room/delete", hasBody = true)
    fun deleteRoom(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

    @POST("room")
    fun getRoomDetail(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<RoomListResponse>

    @POST("room/equipment/list")
    fun getRoomDevices(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<DeviceListResponse>

    @HTTP(method = "PUT", path = "room/equipment/changeBatchRoomToEquipment", hasBody = true)
    fun changeDeviceRoom(@Header("token") token: String, @Body requestBody: ChangeDeviceRoomRequest): Call<SendVerifiedResponse>

//    @GET("room")
//    fun getRoomDevices(@Query("id") id: String):
}