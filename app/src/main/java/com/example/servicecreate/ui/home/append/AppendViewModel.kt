package com.example.servicecreate.ui.home.append

import androidx.lifecycle.ViewModel
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.MainListener
import com.example.servicecreate.ui.toast

/**
 * @author:SunShibo
 * @date:2023-04-01 9:26
 * @feature:
 */
class AppendViewModel: ViewModel() {

    internal var appendListener: AppendListener?=null
    private val repository = Repository

    internal var deviceId = ""
    internal var deviceName = ""

    private val token = ServiceCreateApplication.appSecret

    fun addRoom(roomName: String){
        val response = repository.addRoom(token, roomName)
        appendListener?.onAddRoom(response)
    }

    fun getRoomList(){
        val result = repository.getRoomList(token)
        appendListener?.onGetRoomList(result)
    }

    fun addDevice(deviceName: String, deviceType: Int){
        val result = repository.addDevice(token, deviceName, deviceType)
        appendListener?.onAddDevice(result)
    }

    fun addDeviceToRoom(roomId: Long, deviceName: String, deviceType: Int) {
        val result = repository.addDevice(token, deviceName, deviceType)
        appendListener?.onAddDeviceToRoom(result, roomId)
    }

    fun checkString(): Boolean{
        if(deviceId.isBlank() || deviceId.isEmpty()){
            "设备Id不能为空".toast()
            return false
        }
        if(deviceName.isBlank() || deviceName.isEmpty()){
            "设备名不能为空".toast()
            return false
        }
        return true
    }


}