package com.example.servicecreate.logic.db.model

import android.bluetooth.BluetoothDevice
import androidx.annotation.DrawableRes

/**
 * @author:SunShibo
 * @date:2023-03-23 23:25
 * @feature:
 */
/**
 * type = 0, 添加房间,
 * 1, 空调
 * 2, 灯,
 * 3, 门锁
 * 4, other
 */

data class AppendItem(
    var type: Int = 0,
    var imageId: Int,
    var name: String
)
/**
 * @param state 开 1, 关 0
 * @param grade 风速 0, 1, 2, 3
 * @param scaveng 扫风
 * @param mode 模式
 * @param temp 温度
 */
data class AirControllerData(
    var state: Int,
    var grade: Int,
    var id: Int,
    var mode: Int,
    var scaveng: Int,
    var temp: Int
)

data class MyDevice(val device: BluetoothDevice, var rssi: Int)
