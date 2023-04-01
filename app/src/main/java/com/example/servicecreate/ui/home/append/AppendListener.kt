package com.example.servicecreate.ui.home.append

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-04-01 15:32
 * @feature:
 */
interface AppendListener {
    fun onGetRoomList(result: LiveData<Result<RoomListResponse>>)
    fun onAddDevice(result: LiveData<Result<SendVerifiedResponse>>)
    fun onAddRoom(result: LiveData<Result<SendVerifiedResponse>>)
}