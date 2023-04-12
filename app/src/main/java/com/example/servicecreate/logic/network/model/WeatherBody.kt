package com.example.servicecreate.logic.network.model

/**
 * @author:SunShibo
 * @date:2023-04-12 19:12
 * @feature:
 */
data class WeatherResponse(
    val result: WeatherResult,
    val success: String
)

data class WeatherResult(
    val area_1: String,
    val area_2: String,
    val area_3: String,
    val cityid: String,
    val realTime: RealTime,
    val today: Today,
    val weaid: String
)

data class RealTime(
    val week: String,
    val wtAqi: String,
    val wtHumi: String,
    val wtIcon: String,
    val wtId: String,
    val wtNm: String,
    val wtPressurel: String,
    val wtRainfall: String,
    val wtTemp: String,
    val wtVisibility: String,
    val wtWindId: String,
    val wtWindNm: String,
    val wtWinp: String,
    val wtWins: String
)

data class Today(
    val wtBlueSkyId: String,
    val wtIcon1: String,
    val wtIcon2: String,
    val wtId1: String,
    val wtId2: String,
    val wtNm1: String,
    val wtNm2: String,
    val wtSunr: String,
    val wtSuns: String,
    val wtTemp1: String,
    val wtTemp2: String,
    val wtWindId1: String,
    val wtWindId2: String,
    val wtWindNm1: String,
    val wtWindNm2: String,
    val wtWinpId1: String,
    val wtWinpId2: String,
    val wtWinpNm1: String,
    val wtWinpNm2: String
)