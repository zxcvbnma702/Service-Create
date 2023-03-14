package com.example.servicecreate.logic.network

import android.util.Log
import com.example.servicecreate.logic.network.api.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author:SunShibo
 * @date:2023-03-04 23:01
 * @feature:
 */
object NetworkCenter {

    private val authServer = ServiceCreator.create<AuthService>()

    suspend fun sendVerifiedCode(request: Map<String, String>)
        = authServer.sendVerifiedCode(request).await()

    suspend fun checkVerifiedCode(request: Map<String, String>)
        = authServer.checkVerifiedCode(request).await()

    suspend fun login(request: Map<String, String>)
        = authServer.login(request).await()

    suspend fun register(request: Map<String, String>)
        = authServer.register(request).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
//                    ToDo: delete this
                    Log.e("data", response.toString())
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}