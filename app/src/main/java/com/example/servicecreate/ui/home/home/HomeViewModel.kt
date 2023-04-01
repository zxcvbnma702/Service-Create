package com.example.servicecreate.ui.home.home

import android.util.Log
import androidx.lifecycle.*
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.MainListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * @author:SunShibo
 * @date:2023-03-04 22:56
 * @feature:
 */
class HomeViewModel : ViewModel() {
    internal var mainListener: MainListener?= null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

    val _refresh = MutableSharedFlow<Int>()

    val _jumpToExhibit = MutableSharedFlow<Long>()

    fun refreshHomePage(random: Int = 0) {
        viewModelScope.launch {
            _refresh.emit(random)
        }
    }

    fun jumpToExhibitPage(jump: Long = 0) {
        viewModelScope.launch {
            _jumpToExhibit.emit(jump)
        }
    }

    fun getRoomList(){
        val result = repository.getRoomList(token)
        mainListener?.onGetRoomList(result)
    }

    fun deleteRoom(roomId: String){
        val response = repository.deleteRoom(token, roomId)
        mainListener?.onDeleteRoom(response)
    }

    fun getDeviceKind(){
        val response = repository.getDeviceKindList(token)
        mainListener?.onGetDeviceKindList(response)
    }

}