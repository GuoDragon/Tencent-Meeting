package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.ui.components.FunctionButton

/**
 * 底部功能栏组件
 * 包含麦克风、摄像头、共享屏幕、成员管理按钮
 */
@Composable
fun BottomFunctionBar(
    micEnabled: Boolean,
    videoEnabled: Boolean,
    isScreenSharing: Boolean,
    participantCount: Int,
    onMicClick: () -> Unit,
    onVideoClick: () -> Unit,
    onShareScreenClick: () -> Unit,
    onManageMemberClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F2227))
            .padding(vertical = 16.dp, horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FunctionButton(
            icon = if (micEnabled) Icons.Default.Mic else Icons.Default.MicOff,
            text = if (micEnabled) stringResource(R.string.device_mic_mute) else stringResource(R.string.device_mic_unmute),
            iconTint = if (micEnabled) Color.White else Color.Red,
            onClick = onMicClick
        )

        FunctionButton(
            icon = if (videoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
            text = if (videoEnabled) stringResource(R.string.device_video_disable) else stringResource(R.string.device_video_enable),
            iconTint = if (videoEnabled) Color.White else Color.Red,
            onClick = onVideoClick
        )

        FunctionButton(
            icon = Icons.Default.ScreenShare,
            text = stringResource(R.string.meeting_share_screen),
            iconTint = if (isScreenSharing) Color(0xFF4CAF50) else Color.White,
            onClick = onShareScreenClick
        )

        FunctionButton(
            icon = Icons.Default.People,
            text = stringResource(R.string.meeting_manage_members_count, participantCount),
            iconTint = Color.White,
            onClick = onManageMemberClick
        )
    }
}
