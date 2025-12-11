package com.example.tencentmeeting.model

/**
 * 会议邀请数据模型
 * 用于管理会议成员邀请状态
 */
data class MeetingInvitation(
    val invitationId: String,         // 邀请唯一标识
    val meetingId: String,            // 目标会议ID
    val inviterId: String,            // 邀请人ID（主持人）
    val inviteeId: String,            // 被邀请人ID
    val status: InvitationStatus,     // 邀请状态
    val invitedTime: Long,            // 邀请时间���
    val respondedTime: Long? = null   // 响应时间戳
)

/**
 * 邀请状态枚举
 */
enum class InvitationStatus {
    PENDING,    // 待响应（已发送，等待对方响应）
    ACCEPTED,   // 已接受（对方同意加入）
    REJECTED,   // 已拒绝（对方拒绝加入）
    EXPIRED     // 已过期（邀请超时）
}