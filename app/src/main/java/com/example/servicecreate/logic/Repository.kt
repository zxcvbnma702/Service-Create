package com.example.servicecreate.logic

import androidx.lifecycle.liveData
import kotlin.coroutines.CoroutineContext

/**
 * @author:SunShibo
 * @date:2023-03-04 23:03
 * @feature:
 */
object Repository {


    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}