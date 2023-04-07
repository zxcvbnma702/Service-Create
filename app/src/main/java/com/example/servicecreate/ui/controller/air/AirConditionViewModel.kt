package com.example.servicecreate.ui.controller.air

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.logic.db.model.AirControllerData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-03-19 18:45
 * @feature:
 */
class AirConditionViewModel: ViewModel() {
    internal var airConditionListener: AirConditionListener?= null

    private val repository =  Repository
    private val token = ServiceCreateApplication.appSecret

    internal var id: Int = 2722
    internal var airControllerData = AirControllerData(0, 0, 0, 0, 0, 0)
    internal val _airController = MutableSharedFlow<AirControllerData>()

    /**
     * @param state 开 1, 关 0
     * @param grade 风速 0, 1, 2, 3
     * @param scaveng 扫风  上下 0, 左右 1, 自动 2
     * @param mode 模式 制热 0, 制冷 1, 除湿 2, 通风 3
     * @param temp 温度 1 - 100
     */
    fun sendAirController(){
        viewModelScope.launch {
            _airController.emit(airControllerData)
        }
    }

    fun sendControllerToNet(){
        val result = repository.airController(token, airControllerData.id, airControllerData.state,
            airControllerData.mode, airControllerData.grade, airControllerData.temp, airControllerData.scaveng)
        airConditionListener?.onSendControllerData(result)
    }

    fun getAirDetail(id: Int) {
        val result = repository.getAirDetail(token, id)
        airConditionListener?.onAirDetailData(result)
    }

}