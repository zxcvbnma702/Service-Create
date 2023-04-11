package com.example.servicecreate.ui.controller.doorlock

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.controller.air.AirConditionListener
import com.example.servicecreate.ui.toast
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-04-08 1:12
 * @feature:
 */
class DoorLockViewModel: ViewModel() {
    internal var doorLockListener: DoorLockListener?= null

    private val repository =  Repository
    private val token = ServiceCreateApplication.appSecret

    internal var id: Int = 2723
    internal var state: Int = 0
    internal var newPawd: String = ""

    internal val _doorLockController = MutableSharedFlow<Int>()

    fun sendDoorLockController(){
        viewModelScope.launch {
            _doorLockController.emit(0)
        }
    }

    internal fun checkPawd(): Boolean {
        if (newPawd.length != 4) {
            "请输入四位密码".toast()
            return false
        }
        return true
    }

    fun changePawd(){
        val result = repository.doorLockPawdController(token, id.toString(), newPawd)
        doorLockListener?.onChangePawd(result)
    }

    fun sendControllerToNet(){
        val result = repository.doorLockController(token, id, state)
        doorLockListener?.onSendControllerData(result)
    }

    fun getDoorLockDetail(id: Int){
        val result = repository.getDoorLockDetail(token, id)
        doorLockListener?.onDoorLockDetailData(result)
    }

}