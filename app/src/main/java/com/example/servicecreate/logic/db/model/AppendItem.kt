package com.example.servicecreate.logic.db.model

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
