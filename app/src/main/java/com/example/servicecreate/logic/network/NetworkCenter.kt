package com.example.servicecreate.logic.network

import android.location.LocationRequest
import android.util.Log
import com.example.servicecreate.logic.network.api.*
import com.example.servicecreate.logic.network.model.AddDeviceToRoomRequest
import com.example.servicecreate.logic.network.model.ChangeDeviceRoomRequest
import com.example.servicecreate.logic.network.model.ScheduleBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author:SunShibo
 * @date:2023-03-04 23:01
 * @feature:
 */
object NetworkCenter {

    private val authServer = ServiceCreator.create<AuthService>()
    private val roomServer = ServiceCreator.create<RoomService>()
    private val deviceServer = ServiceCreator.create<DeviceService>()
    private val controllerServer = ServiceCreator.create<ControllerService>()
    private val weatherController = ServiceCreator.create<WeatherService>()

    /**
     * Auth
     */
    suspend fun sendVerifiedCode(request: Map<String, String>)
        = authServer.sendVerifiedCode(request).await()

    suspend fun checkVerifiedCode(request: Map<String, String>)
        = authServer.checkVerifiedCode(request).await()

    suspend fun login(request: Map<String, String>)
        = authServer.login(request).await()

    suspend fun register(request: Map<String, String>)
        = authServer.register(request).await()

    suspend fun sendMac(token: String, request: Map<String, String>)
        = authServer.sendMac(token, request).await()

    /**
     * Room
     */
    suspend fun getRoomList(token: String)
        = roomServer.getRoomList(token).await()

    suspend fun addRoom(token: String, request: Map<String, String>)
        = roomServer.addRoom(token, request).await()

    suspend fun deleteRoom(token: String, request: Map<String, String>)
            = roomServer.deleteRoom(token, request).await()

    suspend fun getRoomDetail(token: String, request: Map<String, String>)
            = roomServer.getRoomDetail(token, request).await()

    suspend fun getRoomDevices(token: String, request: Map<String, String>)
            = roomServer.getRoomDevices(token, request).await()

    suspend fun changeDeviceRoom(token: String, request: ChangeDeviceRoomRequest)
            = roomServer.changeDeviceRoom(token, request).await()

    /**
     * Device
     */
    suspend fun getDeviceKind(token: String)
        = deviceServer.getDeviceKind(token).await()

    suspend fun addDevice(token: String, request: Map<String, String>)
            = deviceServer.addDevice(token, request).await()

    suspend fun addDeviceToRoom(token: String, request: AddDeviceToRoomRequest)
            = deviceServer.addDeviceToRoom(token, request).await()

    suspend fun findDevice(token: String)
            = deviceServer.findDevice(token).await()

    suspend fun getDeviceList(token: String)
            = deviceServer.getDeviceList(token).await()

    suspend fun getDeviceListByType(token: String, type: Int)
            = deviceServer.getDeviceListByType(token, type).await()

    suspend fun deleteDevice(token: String, request: Map<String, String>)
            = deviceServer.deleteDevice(token, request).await()

    suspend fun getAirDetail(token: String, id: Int)
            = deviceServer.getAirState(token, id).await()

    suspend fun getLampDetail(token: String, id: Int)
            = deviceServer.getLampState(token, id).await()

    suspend fun getDoorLockDetail(token: String, id: Int)
            = deviceServer.getDoorLockState(token, id).await()

    /**
     * Controller
     */
    suspend fun airController(token: String, request: Map<String, Int>)
            = controllerServer.airController(token, request).await()

    suspend fun lampController(token: String, request: Map<String, Int>)
            = controllerServer.lampController(token, request).await()

    suspend fun doorLockController(token: String, request: Map<String, Int>)
            = controllerServer.doorLockController(token, request).await()

    suspend fun doorLockPawdController(token: String, request: Map<String, String>)
            = controllerServer.doorLockControllerPawd(token, request).await()

    suspend fun ledController(token: String, request: Map<String, String>)
            = controllerServer.ledController(token, request).await()

    suspend fun controllerRoomAllDevice(token: String, request: Map<String, String>)
            = controllerServer.controllerRoomAllDevice(token, request).await()

    suspend fun controllerSleep(token: String)
            = controllerServer.controllerSleep(token).await()

    suspend fun controllerIndoor(token: String)
            = controllerServer.controllerIndoor(token).await()

    suspend fun controllerOutDoor(token: String)
            = controllerServer.controllerOutdoor(token).await()

    suspend fun controllerScheduledOpen(token: String, request: ScheduleBody)
            = controllerServer.scheduleOpen(token, request).await()

    suspend fun getScheduleList(token: String)
            = controllerServer.scheduleList(token).await()

    suspend fun deleteSchedule(token: String, request: Map<String, String>)
            = controllerServer.deleteSchedule(token, request).await()

    suspend fun getWeather()
            = weatherController.weather().await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
//                    ToDo: delete this
                    Log.e("NetworkCenter", response.toString())
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}