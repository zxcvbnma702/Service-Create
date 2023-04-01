package com.example.servicecreate.ui.home

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.*

/**
 * @author:SunShibo
 * @date:2023-03-04 22:56
 * @feature:
 */
interface MainListener {
    fun onGetRoomList(result: LiveData<Result<RoomListResponse>>){}
    fun refreshHomepage(){}
    fun onDeleteRoom(result: LiveData<Result<SendVerifiedResponse>>){}
    fun onGetDeviceKindList(result: LiveData<Result<RoomListResponse>>) {}
}