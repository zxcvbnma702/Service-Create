package com.example.servicecreate.ui.controller.doorlock

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.DoorLockDetailResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-04-08 1:13
 * @feature:
 */
interface DoorLockListener {
    fun onSendControllerData(result: LiveData<Result<SendVerifiedResponse>>)
    fun onDoorLockDetailData(result: LiveData<Result<DoorLockDetailResponse>>)
    fun onChangePawd(result: LiveData<Result<SendVerifiedResponse>>)
}