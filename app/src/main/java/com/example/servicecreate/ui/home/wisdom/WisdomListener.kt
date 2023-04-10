package com.example.servicecreate.ui.home.wisdom

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-04-10 11:03
 * @feature:
 */
interface WisdomListener {
    fun onIndoorController(result: LiveData<Result<SendVerifiedResponse>>)
    fun onOutdoorController(result: LiveData<Result<SendVerifiedResponse>>)
    fun onSleepController(result: LiveData<Result<SendVerifiedResponse>>)
}