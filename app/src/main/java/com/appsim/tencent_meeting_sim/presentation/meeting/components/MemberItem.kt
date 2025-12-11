package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsim.tencent_meeting_sim.data.model.User
import com.example.tencent_meeting_sim.R

/**
 * 成员列表项组件
 * 显示成员头像、姓名、麦克风和摄像头状态
 */
@Composable
fun MemberItem(
    member: User,
    micEnabled: Boolean,
    videoEnabled: Boolean,
    allMicsMuted: Boolean
) {
    // 判断麦克风是否应该显示为静音状态
    val shouldShowMicMuted = allMicsMuted || !micEnabled

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF4A5568)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.username.take(1),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 姓名和标签
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = member.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "(主持人，我)",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // 麦克风图标
        Icon(
            imageVector = if (shouldShowMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
            contentDescription = stringResource(if (shouldShowMicMuted) R.string.device_mic_disable else R.string.device_mic_enable),
            tint = if (shouldShowMicMuted) Color.Red else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 摄像头图标
        Icon(
            imageVector = if (videoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
            contentDescription = stringResource(if (videoEnabled) R.string.device_camera_enable else R.string.device_camera_disable),
            tint = if (videoEnabled) Color.Gray else Color.Red,
            modifier = Modifier.size(24.dp)
        )
    }
}
