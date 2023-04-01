package com.example.servicecreate.ui.home.append

import androidx.lifecycle.ViewModel
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.MainListener

/**
 * @author:SunShibo
 * @date:2023-04-01 9:26
 * @feature:
 */
class AppendViewModel: ViewModel() {

    internal var mainListener: MainListener?= null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret

    fun addRoom(roomName: String){
        val response = repository.addRoom(token, roomName)
        mainListener?.onAddRoom(response)
    }


}