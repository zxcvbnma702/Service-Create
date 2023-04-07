package com.example.servicecreate.ui.controller.light

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.AirDetailResponse
import com.example.servicecreate.logic.network.model.LampDetailResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-03-23 18:59
 * @feature:
 */
interface LightListener {
    fun onSendControllerData(result: LiveData<Result<SendVerifiedResponse>>)
    fun onLampDetailData(result: LiveData<Result<LampDetailResponse>>)
}