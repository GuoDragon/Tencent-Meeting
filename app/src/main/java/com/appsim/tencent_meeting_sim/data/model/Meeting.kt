package com.appsim.tencent_meeting_sim.data.model

/**
 * 会议记录数据模型
 * 用于维护会议信息，支持快速会议和预约会议
 * 在"我的会议页"中展示，包括已结束的和待开始的会议
 */
data class Meeting(
    val meetingId: String,                          // 会议号（唯一标识）
    val topic: String,                              // 会议主题
    val password: String? = null,                   // 会议密码（可选）
    val hostId: String,                             // 主持人用户ID
    val startTime: Long,                            // 开始时间（时间戳）
    val endTime: Long? = null,                      // 结束时间（时间戳，可选）
    val status: MeetingStatus,                      // 会议状态
    val meetingType: MeetingType,                   // 会议类型（快速会议/预约会议）
    val participantIds: List<String> = emptyList(), // 参会人员ID列表
    val settings: MeetingSettings = MeetingSettings(), // 会议设置
    val isLocked: Boolean = false,                  // 是否锁定会议（锁定后新成员无法加入）
    val isRecording: Boolean = false,               // 是否正在录制
    val recordingStartTime: Long? = null            // 录制开始时间（时间戳，可选）
)
