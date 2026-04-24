package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.data.model.Message
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageItem(message: Message) {
    val context = LocalContext.current
    val isMyMessage = message.senderName == context.getString(R.string.label_me)
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = if (isMyMessage) Alignment.End else Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start) {
            Text(text = message.senderName, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = timeFormat.format(Date(message.timestamp)), fontSize = 12.sp, color = Color.LightGray)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Surface(shape = RoundedCornerShape(topStart = if (isMyMessage) 12.dp else 4.dp, topEnd = if (isMyMessage) 4.dp else 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp), color = if (isMyMessage) Color(0xFF1976D2) else Color.White, shadowElevation = 2.dp) {
            Text(text = message.content, modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp), color = if (isMyMessage) Color.White else Color.Black, fontSize = 15.sp)
        }
    }
}
