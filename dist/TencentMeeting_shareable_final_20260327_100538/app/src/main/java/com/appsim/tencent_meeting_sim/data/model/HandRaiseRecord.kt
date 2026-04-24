package com.appsim.tencent_meeting_sim.data.model

/**
 * 举手记录数据模型
 * 用于记录和检查参会人员的举手操作
 * 支持GUI Agent任务完成情况检查
 */
data class HandRaiseRecord(
    val recordId: String,               // 记录唯一标识
    val meetingId: String,              // 所属会议ID
    val userId: String,                 // 举手用户ID
    val userName: String,               // 举手用户姓名
    val raiseTime: Long,                // 举手时间（时间戳）
    val lowerTime: Long? = null         // 放下手的时间（时间戳，null表示仍在举手）
)
