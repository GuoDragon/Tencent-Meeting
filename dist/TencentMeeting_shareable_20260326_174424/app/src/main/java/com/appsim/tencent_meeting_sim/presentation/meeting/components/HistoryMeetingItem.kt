package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.data.model.Meeting
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryMeetingItem(meeting: Meeting, onClick: () -> Unit) {
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA)
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() }, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(text = meeting.meetingId, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = meeting.topic, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = stringResource(R.string.icon_desc_view_details), tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = context.getString(R.string.label_time_with_value, timeFormat.format(Date(meeting.endTime ?: meeting.startTime))), fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = context.getString(R.string.label_host_with_value, "刘承龙"), fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
