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

    val _refresh = MutableSharedFlow<Int>()

    fun getRoomDetail(id : Long){
        val result = repository.getRoomDetail(token, id)
        exhibitListener?.onRoomDetail(result)
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


}