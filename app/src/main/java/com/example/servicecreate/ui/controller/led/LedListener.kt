package com.example.servicecreate.ui.controller.led

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-04-11 21:27
 * @feature:
 */
interface LedListener {
    fun onSendColorControllerData(result: LiveData<Result<SendVerifiedResponse>>)
    fun onSendStateControllerData(result: LiveData<Result<SendVerifiedResponse>>)

}