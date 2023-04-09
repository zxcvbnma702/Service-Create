package com.example.servicecreate.logic.network.api

import com.example.servicecreate.logic.network.model.LoginResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @author:SunShibo
 * @date:2023-03-12 23:57
 * @feature:
 */
interface AuthService {

//    @Headers("appId: ${FishApplication.appId}", "appSecret: ${FishApplication.appSecret}")
//    @POST("goods/save")
//    fun saveGoodInformation(@Body requestBody: Map<String, String>): Call<SaveResponse>

    @POST("user/sendMsg")
    fun sendVerifiedCode(@Body requestBody: Map<String, String>):Call<SendVerifiedResponse>

    @POST("user/checkCode")
    fun checkVerifiedCode(@Body requestBody: Map<String, String>):Call<SendVerifiedResponse>

    @POST("user/login")
    fun login(@Body requestBody: Map<String, String>):Call<LoginResponse>

    @POST("user/register")
    fun register(@Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

    @POST("userMAC")
    fun sendMac(@Header("token") token: String, @Body requestBody: Map<String, String>): Call<SendVerifiedResponse>

}