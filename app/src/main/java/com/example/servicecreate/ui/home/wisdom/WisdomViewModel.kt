package com.example.servicecreate.ui.home.wisdom

import androidx.lifecycle.ViewModel
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository

/**
 * @author:SunShibo
 * @date:2023-04-09 19:36
 * @feature:
 */
class WisdomViewModel: ViewModel() {
    internal var wisdomListener: WisdomListener?= null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

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
}