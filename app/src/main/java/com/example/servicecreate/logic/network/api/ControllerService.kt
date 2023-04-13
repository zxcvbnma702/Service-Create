package com.example.servicecreate.logic.network.api

import com.example.servicecreate.logic.network.model.ScheduleBody
import com.example.servicecreate.logic.network.model.ScheduleTaskResponse
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

    @POST("controller/gate")
    fun doorLockController(@Header("token") token: String, @Body requestBody: Map<String, Int>): Call<SendVerifiedResponse>

    @POST("controller/gate/password")
    fun doorLockControllerPawd(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

    @POST("controller/led/RGB")
    fun ledController(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>


//    {
//        "roomId": null,
//        "kindId": 2,
//        "state": 0
//    }
    @POST("controller/room")
    fun controllerRoomAllDevice(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

    @POST("controller/indoor")
    fun controllerIndoor(@Header("token") token: String): Call<SendVerifiedResponse>

    @POST("controller/outdoor")
    fun controllerOutdoor(@Header("token") token: String): Call<SendVerifiedResponse>

    @POST("controller/sleep")
    fun controllerSleep(@Header("token") token: String): Call<SendVerifiedResponse>

    @POST("controller/scheduledOpen")
    fun scheduleOpen(@Header("token") token: String, @Body requestBody: ScheduleBody): Call<SendVerifiedResponse>

    @POST("scheduled/list")
    fun scheduleList(@Header("token") token: String): Call<ScheduleTaskResponse>

    @HTTP(method = "DELETE", path = "scheduled", hasBody = true)
    fun deleteSchedule(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>
}