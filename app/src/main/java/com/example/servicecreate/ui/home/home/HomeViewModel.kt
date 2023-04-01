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

    fun refreshHomePage(random: Int = 0) {
        viewModelScope.launch {
            _refresh.emit(random)
        }
    }

    fun getRoomList(){
        Log.e("getRoom", "get")
        val result = repository.getRoomList(token)
        mainListener?.onGetRoomList(result)
    }

    fun delete(roomId: String){
        val response = repository.deleteRoom(token, roomId)
        mainListener?.onDeleteRoom(response)
    }

}