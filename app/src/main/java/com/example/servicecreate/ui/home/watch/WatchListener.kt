package com.example.servicecreate.ui.home.watch

import androidx.lifecycle.LiveData
import com.example.servicecreate.logic.network.model.BraceletResponse

/**
 * @author:SunShibo
 * @date:2023-05-21 23:28
 * @feature:
 */
interface WatchListener {
    fun onBraceLet(result: LiveData<Result<BraceletResponse>>)
}