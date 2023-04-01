package com.example.servicecreate.logic.network.api

import androidx.room.Delete
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import retrofit2.Call
import retrofit2.http.*

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

//    @GET("room")
//    fun getRoomDevices(@Query("id") id: String):
}