package com.example.servicecreate.logic.network

import android.util.Log
import com.example.servicecreate.logic.network.api.AuthService
import com.example.servicecreate.logic.network.api.DeviceService
import com.example.servicecreate.logic.network.api.RoomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

    /**
     * Room
     */
    suspend fun getRoomList(token: String)
        = roomServer.getRoomList(token).await()

    suspend fun addRoom(token: String, request: Map<String, String>)
        = roomServer.addRoom(token, request).await()

    suspend fun deleteRoom(token: String, request: Map<String, String>)
            = roomServer.deleteRoom(token, request).await()

    /**
     * Device
     */
    suspend fun getDeviceKind(token: String)
        = deviceServer.getDeviceKind(token).await()

    suspend fun addDevice(token: String, request: Map<String, String>)
            = deviceServer.addDevice(token, request).await()

    suspend fun addDeviceToRoom(token: String, request: Map<String, String>)
            = deviceServer.addDeviceToRoom(token, request).await()


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