package com.example.base.kxt

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * @author:SunShibo
 * @date:2022-08-22 23:12
 * @feature: some ext func
 */

fun Context.toast(content: String) =
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()

fun Context.toast(res: Int) =
    Toast.makeText(this, getString(res), Toast.LENGTH_SHORT).show()

fun Context.toast(res: Int, vararg context: String) {
    var content: String? = null
    for (element in context){
        if(element == " "){
            continue
        }
        content += " , "
        content += element
    }
    Toast.makeText(this, getString(res) + content, Toast.LENGTH_SHORT).show()
}

fun Context.initSp(): SharedPreferences =
    this.getSharedPreferences("LoginStatus", Context.MODE_PRIVATE)

/**
 * We must use the reflection mechanism to load our layout
 * from the inflate method in ActivityMainBinding
 * and use the corresponding inflate method.
 */
inline fun <VB : ViewBinding> Any.getViewBinding(inflater: LayoutInflater, position: Int = 0): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[position].getDeclaredMethod("inflate", LayoutInflater::class.java)
    return inflate.invoke(null, inflater) as VB
}

inline fun <VB : ViewBinding> Any.getViewBinding(
    inflater: LayoutInflater,
    container: ViewGroup?,
    position: Int = 0
): VB {
    val vbClass =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments.filterIsInstance<Class<VB>>()
    val inflate = vbClass[position].getDeclaredMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    )
    return inflate.invoke(null, inflater, container, false) as VB
}



