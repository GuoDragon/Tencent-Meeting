package com.example.tencentmeeting.model

/**
 * 参会人员状态数据模型
 * 用于会议页面跟踪每个参会人员的实时状态
 * 包括静音、摄像头、举手、共享屏幕等状态
 */
data class MeetingParticipant(
    val userId: String,                     // 参会人员用户ID
    val meetingId: String,                  // 所属会议ID
    val isMuted: Boolean = true,            // 是否静音
    val isCameraOn: Boolean = false,        // 是否开启摄像头
    val isHandRaised: Boolean = false,      // 是否举手
    val handRaisedTime: Long? = null,       // 举手时间（时间戳）
    val isSharingScreen: Boolean = false,   // 是否共享屏幕
    val joinTime: Long                      // 加入会议时间（时间戳）
)
