package com.appsim.tencent_meeting_sim.data.model

/**
 * 用户/联系人数据模型
 * 用于通讯录页面展示好友信息
 */
data class User(
    val userId: String,                // 用户唯一标识
    val username: String,              // 用户名
    val avatar: String? = null,        // 头像URL
    val phone: String? = null,         // 电话号码
    val email: String? = null          // 邮箱地址
)
