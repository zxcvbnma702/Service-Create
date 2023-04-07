package com.example.servicecreate.ui.controller.light

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-03-23 19:00
 * @feature:
 */
class LightViewModel: ViewModel() {
    var lightListener: LightListener?= null

    private val repository =  Repository
    private val token = ServiceCreateApplication.appSecret

    internal var id: Int = 2722
    internal var state: Int = 0

    internal val _lampController = MutableSharedFlow<Int>()

    fun sendLampController(){
        viewModelScope.launch {
            _lampController.emit(0)
        }
    }

    fun sendControllerToNet(){
        val result = repository.lampController(token, id, state)
        lightListener?.onSendControllerData(result)
    }

    fun getLampDetail(id: Int){
        val result = repository.getLampDetail(token, id)
        lightListener?.onLampDetailData(result)
    }
}