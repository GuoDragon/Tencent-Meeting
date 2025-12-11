package com.example.tencentmeeting.model

/**
 * 会议状态枚举
 * 用于标识会议的当前状态
 */
enum class MeetingStatus {
    UPCOMING,    // 待开始 - 预约会议尚未到达开始时间
    ONGOING,     // 进行中 - 会议正在进行
    ENDED        // 已结束 - 会议已经结束
}
