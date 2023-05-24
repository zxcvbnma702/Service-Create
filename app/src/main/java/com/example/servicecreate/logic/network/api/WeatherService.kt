package com.example.servicecreate.logic.network.api

import com.example.servicecreate.logic.network.model.BraceletResponse
import com.example.servicecreate.logic.network.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * @author:SunShibo
 * @date:2023-04-12 19:01
 * @feature:
 */
interface WeatherService {

    @GET("http://api.k780.com/")
    fun weather(@Query("weaId")id: Int = 189, @Query("app") r: String = "weather.realtime",
                @Query("ag")day: String = "today",
                @Query("appkey")key: Int = 68788,
                @Query("sign")sign:String ="c1fc0721510dba675f82ec8defa98de2",
                @Query("format")format:String = "json"): Call<WeatherResponse>
    @GET("bracelet")
    fun bracelet(@Query("id") id: Int = 1): Call<BraceletResponse>
}