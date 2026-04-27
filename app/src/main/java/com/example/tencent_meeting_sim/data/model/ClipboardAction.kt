package com.example.tencent_meeting_sim.data.model

data class ClipboardAction(
    val userId: String,
    val type: String,
    val text: String,
    val timestamp: Long
)
