package com.example.servicecreate.ui.home

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.RoomListResponse
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-03-04 22:56
 * @feature:
 */
interface MainListener {
    fun onGetRoomList(result: LiveData<Result<RoomListResponse>>){}
    fun onAddRoom(result: LiveData<Result<SendVerifiedResponse>>){}
    fun refreshHomepage(){}
    fun onDeleteRoom(result: LiveData<Result<SendVerifiedResponse>>){}
}