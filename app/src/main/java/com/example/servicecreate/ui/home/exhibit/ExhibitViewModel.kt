package com.example.servicecreate.ui.home.exhibit

import androidx.lifecycle.ViewModel
import com.example.servicecreate.ServiceCreateApplication
import com.example.servicecreate.logic.Repository
import com.example.servicecreate.ui.home.MainListener

/**
 * @author:SunShibo
 * @date:2023-04-01 11:23
 * @feature:
 */
class ExhibitViewModel :ViewModel(){
    internal var mainListener: MainListener?= null
    private val repository = Repository

    private val token = ServiceCreateApplication.appSecret


}