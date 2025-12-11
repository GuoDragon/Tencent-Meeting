package com.example.tencentmeeting.model

/**
 * 会议重复频率枚举
 * 用于预定会议时指定会议的重复模式
 */
enum class RecurrenceType(val displayName: String) {
    NONE("不重复"),
    DAILY("每天"),
    WEEKLY("每周"),
    MONTHLY("每月")
}
