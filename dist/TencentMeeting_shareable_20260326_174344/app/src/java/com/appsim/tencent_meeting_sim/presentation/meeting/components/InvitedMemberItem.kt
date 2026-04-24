package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
 * 已邀请成员列表项组件
 * 显示已邀请但未入会的成员信息
 */
@Composable
fun InvitedMemberItem(member: User) {
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
                text = stringResource(R.string.msg_invited),
                fontSize = 12.sp,
                color = Color(0xFF4CAF50)
            )
        }

        // 邀请状态
        Text(
            text = stringResource(R.string.msg_waiting_response),
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .background(
                    Color(0xFFF5F5F5),
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
