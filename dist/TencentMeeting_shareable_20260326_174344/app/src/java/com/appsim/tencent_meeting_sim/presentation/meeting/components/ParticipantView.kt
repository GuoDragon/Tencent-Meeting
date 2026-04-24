package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MicOff
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
 * 参会人视图组件
 * 显示参会人头像、姓名和麦克风状态
 */
@Composable
fun ParticipantView(
    participant: User,
    micEnabled: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4A5568)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = participant.username.take(1),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            if (!micEnabled) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xFFFF6B6B))
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MicOff,
                        contentDescription = stringResource(R.string.device_mic_mute),
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = participant.username,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}
