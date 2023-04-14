package com.example.servicecreate.logic

import android.app.DownloadManager
import androidx.lifecycle.liveData
import com.example.servicecreate.logic.network.NetworkCenter
import com.example.servicecreate.logic.network.model.AddDeviceToRoomRequest
import com.example.servicecreate.logic.network.model.ChangeDeviceRoomRequest
import com.example.servicecreate.logic.network.model.DeviceKindData
import com.example.servicecreate.logic.network.model.ScheduleBody
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

    fun sendMac(token: String, username: String, password: String) = fire(Dispatchers.IO){
        val request = mapOf("username" to username, "password" to password)
        val response = NetworkCenter.sendMac(token, request)
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

    fun getRoomDetail(token: String, roomId: Long) = fire(Dispatchers.IO){
        val request = mapOf("id" to roomId.toString())
        val response = NetworkCenter.getRoomDetail(token, request)
        run {
            Result.success(response)
        }
    }

    fun getRoomDevices(token: String, roomId: Long) = fire(Dispatchers.IO){
        val request = mapOf("id" to roomId.toString())
        val response = NetworkCenter.getRoomDevices(token, request)
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

    fun addDeviceToRoom(token: String, deviceId: String, roomId: String, deviceType: Int, deviceName: String) = fire(Dispatchers.IO){
        val request = AddDeviceToRoomRequest(deviceId, deviceName, listOf(roomId), deviceType)
        val response = NetworkCenter.addDeviceToRoom(token, request)
        run {
            Result.success(response)
        }
    }

    fun changeDeviceRoom(token: String, deviceId: String, roomId: String) = fire(Dispatchers.IO){
        val request = ChangeDeviceRoomRequest(deviceId, listOf(roomId))
        val response = NetworkCenter.changeDeviceRoom(token, request)
        run {
            Result.success(response)
        }
    }

    fun findDevice(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.findDevice(token)
        run {
            Result.success(response)
        }
    }

    fun getDeviceList(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.getDeviceList(token)
        run {
            Result.success(response)
        }
    }

    fun getDeviceListByType(token: String, type: Int) = fire(Dispatchers.IO){
        val response = NetworkCenter.getDeviceListByType(token, type)
        run {
            Result.success(response)
        }
    }

    fun deleteDevice(token: String, deviceId: String) = fire(Dispatchers.IO){
        val request = mapOf("id" to deviceId)
        val response = NetworkCenter.deleteDevice(token, request)
        run {
            Result.success(response)
        }
    }

    fun getAirDetail(token: String, id: Int) = fire(Dispatchers.IO){
        val response = NetworkCenter.getAirDetail(token, id)
        run {
            Result.success(response)
        }
    }

    fun getLampDetail(token: String, id: Int) = fire(Dispatchers.IO){
        val response = NetworkCenter.getLampDetail(token, id)
        run {
            Result.success(response)
        }
    }

    fun getDoorLockDetail(token: String, id: Int) = fire(Dispatchers.IO){
        val response = NetworkCenter.getDoorLockDetail(token, id)
        run {
            Result.success(response)
        }
    }
    /**
     * "id": 2722, "state": 1, "mode": 1, "grade": 0, "temp": 20, "scaveng": 0
     */
    fun airController(token: String, id: Int, state: Int, mode: Int, grade: Int, temp: Int, scaveng: Int) = fire(Dispatchers.IO){
        val request = mapOf("id" to id, "state" to state, "mode" to mode, "grade" to grade, "temp" to temp, "scaveng" to scaveng)
        val response = NetworkCenter.airController(token, request)
        run {
            Result.success(response)
        }
    }

    fun airControllerState(token: String, id: Int, state: Int) = fire(Dispatchers.IO){
        val request = mapOf("id" to id, "state" to state)
        val response = NetworkCenter.airController(token, request)
        run {
            Result.success(response)
        }
    }

    fun lampController(token: String,  id: Int, state: Int)= fire(Dispatchers.IO){
        val request = mapOf("id" to id, "state" to state)
        val response = NetworkCenter.lampController(token, request)
        run {
            Result.success(response)
        }
    }

    fun doorLockController(token: String,  id: Int, state: Int) = fire(Dispatchers.IO){
        val request = mapOf("id" to id, "state" to state)
        val response = NetworkCenter.doorLockController(token, request)
        run {
            Result.success(response)
        }
    }

    fun doorLockPawdController(token: String, id: String, pawd: String) = fire(Dispatchers.IO){
        val request = mapOf("id" to id, "password" to pawd)
        val response = NetworkCenter.doorLockPawdController(token, request)
        run {
            Result.success(response)
        }
    }

    fun ledControllerColor(token: String, id: String, r: String, g: String, b: String, light: String) = fire(Dispatchers.IO){
        val request = mapOf("id" to id, "r" to r, "g" to g, "b" to b, "light" to light)
        val response = NetworkCenter.ledController(token, request)
        run {
            Result.success(response)
        }
    }

    fun ledControllerState(token: String, id: String, state: String, light: String) = fire(Dispatchers.IO){
        val request = mapOf("id" to id, "state" to state, "light" to light)
        val response = NetworkCenter.ledController(token, request)
        run {
            Result.success(response)
        }
    }

    fun controllerRoomAllDevice(token: String, roomId: String, kindData: String, state: String)= fire(Dispatchers.IO){
        val request = mapOf("roomId" to roomId, "state" to state, "kindId" to kindData)
        val response = NetworkCenter.controllerRoomAllDevice(token, request)
        run {
            Result.success(response)
        }
    }

    fun controllerSleep(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.controllerSleep(token)
        run {
            Result.success(response)
        }
    }

    fun controllerIndoor(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.controllerIndoor(token)
        run {
            Result.success(response)
        }
    }

    fun controllerOutDoor(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.controllerOutDoor(token)
        run {
            Result.success(response)
        }
    }

    fun controllerScheduleOpen(token: String, schedule: ScheduleBody) = fire(Dispatchers.IO){
        val response = NetworkCenter.controllerScheduledOpen(token, schedule)
        run {
            Result.success(response)
        }
    }

    fun getScheduleList(token: String) = fire(Dispatchers.IO){
        val response = NetworkCenter.getScheduleList(token)
        run {
            Result.success(response)
        }
    }

    fun deleteSchedule(token: String, name: String) = fire(Dispatchers.IO){
        val request = mapOf("name" to name)
        val response = NetworkCenter.deleteSchedule(token, request)
        run {
            Result.success(response)
        }
    }

    fun getWeather() = fire(Dispatchers.IO){
        val response = NetworkCenter.getWeather()
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