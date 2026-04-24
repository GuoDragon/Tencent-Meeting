package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R

/**
 * 弹幕输入区组件
 * 包含聊天输入入口和举手功能
 */
@Composable
fun DanmuInputArea(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isHandRaised: Boolean = false,
    onHandRaiseClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .width(380.dp)
            .height(48.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF3C4148)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 左侧聊天输入区
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 12.dp)
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Chat,
                contentDescription = stringResource(R.string.action_chat),
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Icon(
                imageVector = Icons.Default.EmojiEmotions,
                contentDescription = stringResource(R.string.action_emoji),
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = stringResource(R.string.placeholder_chat_input),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // 右侧举手按钮
        IconButton(
            onClick = onHandRaiseClick,
            modifier = Modifier
                .padding(end = 4.dp)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PanTool,
                contentDescription = if (isHandRaised) stringResource(R.string.action_hand_lower) else stringResource(R.string.action_hand_raise),
                tint = if (isHandRaised) Color(0xFFFFA726) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
