package com.example.servicecreate.ui.controller.air

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.AirDetailResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-03-19 18:45
 * @feature:
 */
interface AirConditionListener {
    fun onSendControllerData(result: LiveData<Result<SendVerifiedResponse>>)
    fun onAirDetailData(result: LiveData<Result<AirDetailResponse>>)
}