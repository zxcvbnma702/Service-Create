package com.example.servicecreate.ui.home.exhibit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.MainListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-04-01 11:23
 * @feature:
 */
class ExhibitViewModel :ViewModel(){
    internal var exhibitListener: ExhibitListener?= null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

    internal var roomList: MutableMap<String, Long> = mutableMapOf()

    val _refresh = MutableSharedFlow<Int>()
    internal var deviceId: Int = 0

    fun getRoomDetail(id : Long){
        val result = repository.getRoomDetail(token, id)
        exhibitListener?.onRoomDetail(result)
    }

    fun getRoomList(){
        val result = repository.getRoomList(token)
        exhibitListener?.onGetRoomList(result)
    }

    fun getRoomDevices(id: Long){
        val result = repository.getRoomDevices(token, id)
        exhibitListener?.onRoomDevice(result)
    }

    fun getDeviceListByType(type: Int){
        val result = repository.getDeviceListByType(token, type)
        exhibitListener?.onDeviceListByType(result)
    }

    fun deleteDevice(deviceId: String){
        val result = repository.deleteDevice(token, deviceId)
        exhibitListener?.onDeleteDevice(result)
    }

    fun refreshExhibitPage(random: Int = 0) {
        viewModelScope.launch { _refresh.emit(random) }
    }

    fun lampState(id: Int, state: Int){
        val result = repository.lampController(token, id, state)
        exhibitListener?.onSendLampState(result)
    }

    fun airState(id: Int, state: Int){
        val result = repository.airControllerState(token, id, state)
        exhibitListener?.onSendAirState(result)
    }

    fun doorLockState(id: Int, state: Int){
        val result = repository.doorLockController(token, id, state)
        exhibitListener?.onSendDoorLockData(result)
    }

    fun ledState(id: Int, state: Int) {
        val result = repository.ledControllerState(token, id.toString(), state.toString(), (0..255).random().toString())
        exhibitListener?.onSendLedState(result)
    }

    fun controllerAllDeviceByKind(roomId: Long, kindData: Int, state: Int){
        val result = repository.controllerRoomAllDevice(token, roomId.toString(), kindData.toString(), state.toString())
        exhibitListener?.onControllerByKind(result)
    }

    fun changeDeviceRoom(deviceId: String, roomId: String){
        val result = repository.changeDeviceRoom(token, deviceId, roomId)
        exhibitListener?.onChangeDeviceRoom(result)
    }

}