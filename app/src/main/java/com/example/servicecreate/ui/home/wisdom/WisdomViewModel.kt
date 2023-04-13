package com.example.servicecreate.ui.home.wisdom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.logic.db.model.CustomDeviceData
import com.example.servicecreate.logic.network.model.DeviceData
import com.example.servicecreate.logic.network.model.Item
import com.example.servicecreate.logic.network.model.ScheduleBody
import com.example.servicecreate.ui.toast
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
    internal var sendNetList: MutableList<Item> = mutableListOf()

    val _refresh = MutableSharedFlow<Int>()

    fun refreshHomePage(random: Int = 0) {
        viewModelScope.launch {
            _refresh.emit(random)
        }
    }

    internal var taskTime: String = ""
    internal var taskName: String = ""
    internal var onceOrEvery: Int = 0

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

    private fun scheduleOpen(scheduleBody: ScheduleBody){
        val result = repository.controllerScheduleOpen(token, scheduleBody)
        wisdomListener?.onScheduleOpen(result)
    }

    fun getScheduleList(){
        val result = repository.getScheduleList(token)
        wisdomListener?.onGetScheduleList(result)
    }

    fun deleteSchedule(name: String){
        val result = repository.deleteSchedule(token, name)
        wisdomListener?.onDeleteSchedule(result)
    }

    fun checkValid(): Boolean {
        if(taskName.isEmpty() || taskName.isBlank()){
            "任务名不能为空".toast()
            return false
        }
        if(taskTime.isBlank() || taskTime.isEmpty()){
            "任务时间不能为空".toast()
            return false
        }
        if(selectedDeviceList.isEmpty()){
            "任务设备不能为空".toast()
            return false
        }
        val scheduleBody = ScheduleBody(taskTime, sendNetList, taskName, onceOrEvery)
        scheduleOpen(scheduleBody)
        return true
    }
}