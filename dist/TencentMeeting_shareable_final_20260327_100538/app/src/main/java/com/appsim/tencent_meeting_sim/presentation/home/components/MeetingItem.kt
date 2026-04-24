package com.appsim.tencent_meeting_sim.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.data.model.Meeting

@Composable
fun MeetingItem(meeting: Meeting, onNavigateToMeetingDetails: (String) -> Unit, onNavigateToScheduledMeetingDetails: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
            when (meeting.status.name) {
                "ONGOING" -> onNavigateToMeetingDetails(meeting.meetingId)
                "UPCOMING" -> onNavigateToScheduledMeetingDetails(meeting.meetingId)
            }
        },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.VideoCall, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = meeting.topic, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = "${stringResource(R.string.meeting_id_label)}: ${meeting.meetingId}", fontSize = 12.sp, color = Color.Gray)
            }
            Text(
                text = when (meeting.status.name) {
                    "ONGOING" -> stringResource(R.string.meeting_status_ongoing)
                    "UPCOMING" -> stringResource(R.string.meeting_status_upcoming)
                    else -> stringResource(R.string.meeting_status_completed)
                },
                fontSize = 12.sp,
                color = when (meeting.status.name) {
                    "ONGOING" -> Color(0xFF4CAF50)
                    "UPCOMING" -> Color(0xFFFF9800)
                    else -> Color.Gray
                }
            )
        }
    }
}
