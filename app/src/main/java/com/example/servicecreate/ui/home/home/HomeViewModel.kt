package com.example.servicecreate.ui.home.home

import android.util.Log
import androidx.lifecycle.*
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.MainListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-03-04 22:56
 * @feature:
 */
class HomeViewModel : ViewModel() {
    internal var mainListener: MainListener?= null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

    internal var isRoom: Boolean = true
    private var v: Boolean = false

    internal var roomName: String = "默认房间"

    val _refresh = MutableSharedFlow<Int>()

    val _jumpToExhibit = MutableSharedFlow<Long>()
    val _jumpTogateWay = MutableSharedFlow<Int>()
    val _jumpToWisdom = MutableSharedFlow<Int>()

    val _modeShift = MutableSharedFlow<Boolean>()

    fun refreshHomePage(random: Int = 0) {
        viewModelScope.launch {
            _refresh.emit(random)
        }
    }

    fun modeShift() {
        viewModelScope.launch {
            _modeShift.emit(v)
        }
        v = !v
    }

    fun jumpToExhibitPage(jump: Long = 0, name: String) {
        roomName = name
        viewModelScope.launch {
            _jumpToExhibit.emit(jump)
        }
    }

    fun jumpToGateWayPage(random: Int = 0) {
        viewModelScope.launch {
            _jumpTogateWay.emit(random)
        }
    }

    fun jumpToWisdomPage(random: Int = 0) {
        viewModelScope.launch {
            _jumpToWisdom.emit(random)
        }
    }

    fun getRoomList(){
        val result = repository.getRoomList(token)
        mainListener?.onGetRoomList(result)
    }

    fun deleteRoom(roomId: String){
        val response = repository.deleteRoom(token, roomId)
        mainListener?.onDeleteRoom(response)
    }

    fun getDeviceKind(){
        val response = repository.getDeviceKindList(token)
        mainListener?.onGetDeviceKindList(response)
    }

    fun getDeviceList(){
        val result = repository.getDeviceList(token)
        mainListener?.onGetDeviceList(result)
    }

    fun deleteDevice(deviceId: String){
        val result = repository.deleteDevice(token, deviceId)
        mainListener?.onDeleteDevice(result)
    }

    fun lampState(id: Int, state: Int){
        val result = repository.lampController(token, id, state)
        mainListener?.onSendLampState(result)
    }

    fun airState(id: Int, state: Int){
        val result = repository.airControllerState(token, id, state)
        mainListener?.onSendAirState(result)
    }

    fun doorLockState(id: Int, state: Int){
        val result = repository.doorLockController(token, id, state)
        mainListener?.onSendDoorLockData(result)
    }

    fun controllerRoomAllDevice(roomId: String, kindData: String, state: String){
        val result = repository.controllerRoomAllDevice(token, roomId, kindData, state)
        mainListener?.onControllerResult(result)
    }

}