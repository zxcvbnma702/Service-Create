package com.example.servicecreate.ui.controller.led

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.controller.doorlock.DoorLockListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-04-10 12:49
 * @feature:
 */
class LedViewModel: ViewModel() {
    internal var ledListener: LedListener?= null

    private val repository =  Repository
    private val token = ServiceCreateApplication.appSecret

    internal var id: Int = 2723
    internal var state: Int = 0


    internal val _ledController = MutableSharedFlow<Int>()

    fun sendLedController(){
        viewModelScope.launch {
            _ledController.emit(0)
        }
    }

    fun sendControllerToNet(a: Int, r: Int, g: Int, b: Int) {
        val result = repository.ledControllerColor(token, id.toString(), a.toString(), r.toString(), g.toString(), b.toString())
        ledListener?.onSendColorControllerData(result)
    }

    fun sendStateControllerToNet() {
        val result = repository.ledControllerState(token, id.toString(), state.toString())
        Log.e("rrr", id.toString() + state.toString())
        ledListener?.onSendStateControllerData(result)
    }

    // TODO: led 状态初始化 
//    fun getDoorLockDetail(id: Int){
//        val result = repository.getDoorLockDetail(token, id)
//        ledListener?.onLedDetailData(result)
//    }


}