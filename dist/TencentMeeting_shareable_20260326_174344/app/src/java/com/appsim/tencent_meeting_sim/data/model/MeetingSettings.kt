package com.appsim.tencent_meeting_sim.data.model

/**
 * 会议设置数据模型
 * 用于快速会议页和预约会议页的会议配置
 */
data class MeetingSettings(
    val allowParticipantUnmute: Boolean = true,     // 允许参会人员自我解除静音
    val muteOnEntry: Boolean = true,                // 入会时自动静音
    val cameraOffOnEntry: Boolean = true,           // 入会时关闭摄像头
    val enableWaitingRoom: Boolean = false          // 启用等候室功能
)
