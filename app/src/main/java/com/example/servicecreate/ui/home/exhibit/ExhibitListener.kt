package com.example.servicecreate.ui.home.exhibit

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-04-03 22:25
 * @feature:
 */
interface ExhibitListener {
    fun onRoomDetail(result: LiveData<Result<RoomListResponse>>)
    fun onRoomDevice(result: LiveData<Result<DeviceListResponse>>)
    fun onDeviceListByType(result: LiveData<Result<DeviceListResponse>>)
    fun onDeleteDevice(result: LiveData<Result<SendVerifiedResponse>>)
    fun onSendLampState(result: LiveData<Result<SendVerifiedResponse>>)
    fun onSendAirState(result: LiveData<Result<SendVerifiedResponse>>)

}