package com.appsim.tencent_meeting_sim.common.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期时间工具类
 */
object DateTimeUtils {
    /**
     * 格式化时间戳为 "MM月dd日 HH:mm" 格式
     */
    fun formatDateTime(timeMillis: Long): String {
        val sdf = SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA)
        return sdf.format(Date(timeMillis))
    }
}
