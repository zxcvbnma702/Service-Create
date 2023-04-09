package com.example.servicecreate.ui.home.gateway

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.SendVerifiedResponse

/**
 * @author:SunShibo
 * @date:2023-04-09 1:04
 * @feature:
 */
interface GatewayListener {
    fun onSendMac(result: LiveData<Result<SendVerifiedResponse>>)
}