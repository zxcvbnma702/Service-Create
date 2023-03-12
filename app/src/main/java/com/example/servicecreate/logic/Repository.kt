package com.example.servicecreate.logic

import android.accounts.NetworkErrorException
import androidx.lifecycle.liveData
import com.example.servicecreate.logic.network.NetworkCenter
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * @author:SunShibo
 * @date:2023-03-04 23:03
 * @feature:
 */
object Repository {

    fun sendVerifiedCode(email: String) = fire(Dispatchers.IO){
        val request = mapOf("qq" to email)
        val response = NetworkCenter.sendVerifiedCode(request)
        run {
            Result.success(response)
        }
    }

    fun checkVerifiedCode(email: String, code: String) = fire(Dispatchers.IO){
        val request = mapOf("qq" to email, "code" to code)
        val response = NetworkCenter.checkVerifiedCode(request)
        run {
            Result.success(response)
        }
    }

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