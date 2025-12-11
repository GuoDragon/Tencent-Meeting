package com.example.tencentmeeting.model

/**
 * 聊天消息数据模型
 * 用于会议聊天页面显示文字消息
 */
data class Message(
    val messageId: String,          // 消息唯一标识
    val meetingId: String,          // 所属会议ID
    val senderId: String,           // 发送者用户ID
    val senderName: String,         // 发送者姓名
    val content: String,            // 消息内容
    val timestamp: Long             // 发送时间（时间戳）
)
