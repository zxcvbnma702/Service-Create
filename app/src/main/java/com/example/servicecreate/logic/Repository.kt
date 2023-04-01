package com.example.servicecreate.logic

import androidx.lifecycle.liveData
import com.example.servicecreate.logic.network.NetworkCenter
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * @author:SunShibo
 * @date:2023-03-04 23:03
 * @feature:
 */
object Repository {

    fun sendVerifiedCode(email: String) = fire(Dispatchers.IO){
        val request = mapOf("qq" to email)
        val response = NetworkCenter.sendVerifiedCode(request)
        run {
            Result.success(response)
        }
    }

    fun checkVerifiedCode(email: String, code: String) = fire(Dispatchers.IO){
        val request = mapOf("qq" to email, "code" to code)
        val response = NetworkCenter.checkVerifiedCode(request)
        run {
            Result.success(response)
        }
    }

    fun login(email: String, password: String) = fire(Dispatchers.IO){
        val request = mapOf("qq" to email, "password" to password)
        val response = NetworkCenter.login(request)
        run {
            Result.success(response)
        }
    }

    fun register(email: String, phone: String, password: String, code: String) = fire(Dispatchers.IO){
        val request = mapOf("qq" to email, "phone" to phone, "password" to password, "code" to code)
        val response = NetworkCenter.register(request)
        run {
            Result.success(response)
        }
    }

    fun getRoomList(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.getRoomList(token)
        run {
            Result.success(response)
        }
    }

    fun addRoom(token: String, roomName: String) = fire(Dispatchers.IO){
        val request = mapOf("name" to roomName)
        val response = NetworkCenter.addRoom(token, request)
        run {
            Result.success(response)
        }
    }

    fun deleteRoom(token: String, roomId: String) = fire(Dispatchers.IO){
        val request = mapOf("id" to roomId)
        val response = NetworkCenter.deleteRoom(token, request)
        run {
            Result.success(response)
        }
    }

    fun getDeviceKindList(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.getDeviceKind(token)
        run {
            Result.success(response)
        }
    }

    fun addDevice(token: String, deviceName: String, deviceType: Int) = fire(Dispatchers.IO){
        val request = mapOf("name" to deviceName, "type" to deviceType.toString())
        val response = NetworkCenter.addDevice(token, request)
        run {
            Result.success(response)
        }
    }

    fun addDeviceToRoom(token: String, deviceId: String, roomId: String) = fire(Dispatchers.IO){
        val request = mapOf("roomId" to roomId, "equipmentId" to deviceId)
        val response = NetworkCenter.addDeviceToRoom(token, request)
        run {
            Result.success(response)
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

}