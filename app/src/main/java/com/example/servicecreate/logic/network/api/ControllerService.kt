package com.example.servicecreate.logic.network.api

import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * @author:SunShibo
 * @date:2023-04-06 23:58
 * @feature:
 */
interface ControllerService {

//    @GET("equipment/conditioner")
//    fun getAirState(@Header("token") token: String, @Query("id") id: String): Call<>

    /**
    "id": 2722,
    "state": 1,
    "mode": 1,
    "grade": 0,
    "temp": 20,
    "scaveng": 0
     */
    @POST("controller/conditioner")
    fun airController(@Header("token") token: String, @Body requestBody: Map<String, Int>): Call<SendVerifiedResponse>

    @POST("controller/light")
    fun lampController(@Header("token") token: String, @Body requestBody: Map<String, Int>): Call<SendVerifiedResponse>

//    {
//        "roomId": null,
//        "kindId": 2,
//        "state": 0
//    }
    @POST("controller/room")
    fun controllerRoomAllDevice(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>



}