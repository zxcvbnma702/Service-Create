package com.example.servicecreate.ui.home.wisdom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.logic.db.model.CustomDeviceData
import com.example.servicecreate.logic.network.model.DeviceData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-04-09 19:36
 * @feature:
 */
class WisdomViewModel: ViewModel() {
    internal var wisdomListener: WisdomListener?= null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

    internal var random: Boolean = false

    internal var allDeviceMap: MutableMap<String, String> = mutableMapOf()
    internal var selectedDeviceList: MutableList<CustomDeviceData> = mutableListOf(CustomDeviceData("", "0"))
    internal var sendNetMap: MutableMap<CustomDeviceData, Boolean> = mutableMapOf()

    val _refresh = MutableSharedFlow<Int>()

    fun refreshHomePage(random: Int = 0) {
        viewModelScope.launch {
            _refresh.emit(random)
        }
    }

    internal var taskTime: String = ""
    internal var taskName: String = ""

    fun controllerIndoor(){
        val result = repository.controllerIndoor(token)
        wisdomListener?.onIndoorController(result)
    }

    fun controllerOutdoor(){
        val result = repository.controllerOutDoor(token)
        wisdomListener?.onOutdoorController(result)
    }

    fun controllerSleep(){
        val result = repository.controllerSleep(token)
        wisdomListener?.onSleepController(result)
    }

    fun getDeviceList(){
        val result = repository.getDeviceList(token)
        wisdomListener?.onGetDeviceList(result)
    }

    fun
}