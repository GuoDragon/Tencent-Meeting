package com.appsim.tencent_meeting_sim.data.model

/**
 * 个人会议室数据模型
 * Personal Meeting Room data model
 */
data class PersonalMeetingRoom(
    val userId: String,                          // 用户ID
    val meetingId: String,                       // 个人会议室号码
    val meetingLink: String,                     // 会议链接
    var password: String? = null,                // 入会密码（可选）
    var enablePassword: Boolean = false,         // 是否启用密码
    var enableWaitingRoom: Boolean = false,      // 是否启用等候室
    var allowBeforeHost: Boolean = false,        // 是否允许成员在主持人前入会
    var enableWatermark: Boolean = false,        // 是否启用会议水印
    var muteOnEntry: String = "超过6人后自动开启", // 成员入会时静音规则
    var allowMultiDevice: Boolean = false        // 是否允许成员多端入会
)
