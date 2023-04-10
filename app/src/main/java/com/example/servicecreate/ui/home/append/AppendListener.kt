package com.example.servicecreate.ui.home.append

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.DeviceListResponse
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-04-01 15:32
 * @feature:
 */
interface AppendListener {
    fun onGetRoomList(result: LiveData<Result<RoomListResponse>>)
    fun onAddRoom(result: LiveData<Result<SendVerifiedResponse>>)
    fun onAddDeviceToRoom(result: LiveData<Result<SendVerifiedResponse>>)
    fun onFindDevice(result: LiveData<Result<DeviceListResponse>>)
    fun onSendMac(result: LiveData<Result<SendVerifiedResponse>>)
}