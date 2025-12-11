package com.example.tencentmeeting.model

/**
 * 会议类型枚举
 * 用于区分快速会议和预约会议
 */
enum class MeetingType {
    INSTANT,     // 快速会议 - 立即开始的会议
    SCHEDULED    // 预约会议 - 需要预约时间的会议
}
