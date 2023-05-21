package com.example.servicecreate.ui.home.person

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-05-21 20:39
 * @feature:
 */
interface PersonListener {
    fun onChangeOver(result: LiveData<Result<SendVerifiedResponse>>)
    fun onSendVerified(result: LiveData<Result<SendVerifiedResponse>>)
}