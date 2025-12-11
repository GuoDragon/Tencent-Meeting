package com.appsim.tencent_meeting_sim.presentation.meeting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingDetailsContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingDetailsPresenter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScheduledMeetingDetailsScreen(meetingId: String, onNavigateBack: () -> Unit = {}, onNavigateToMeetingDetails: (String) -> Unit = {}) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { ScheduledMeetingDetailsPresenter(dataRepository) }
    var currentMeeting by remember { mutableStateOf<Meeting?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : ScheduledMeetingDetailsContract.View {
            override fun showMeetingDetails(meeting: Meeting) { currentMeeting = meeting }
            override fun showError(message: String) { errorMessage = message }
            override fun navigateToMeetingDetails(meetingId: String) { onNavigateToMeetingDetails(meetingId) }
        }
    }

    LaunchedEffect(meetingId) { presenter.attachView(view); presenter.loadMeetingDetails(meetingId) }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5)))).padding(top = 32.dp)) {
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFE3F2FD)).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.icon_desc_back), modifier = Modifier.size(24.dp).clickable { onNavigateBack() }, tint = Color.Black)
            Text(text = stringResource(R.string.meeting_details), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.weight(1f).padding(horizontal = 16.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            Spacer(modifier = Modifier.size(24.dp))
        }

        Column(modifier = Modifier.fillMaxWidth().weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            currentMeeting?.let { mtg ->
                Text(text = mtg.topic, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(vertical = 16.dp))

                val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
                val startDate = Date(mtg.startTime)
                val endTime = mtg.endTime?.let { Date(it) }

                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(text = timeFormat.format(startDate), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                            Text(text = dateFormat.format(startDate), fontSize = 14.sp, color = Color.Gray)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = stringResource(R.string.meeting_status_upcoming), fontSize = 12.sp, color = Color(0xFFFF9800))
                            if (endTime != null) {
                                val duration = (endTime.time - startDate.time) / (1000 * 60)
                                Text(text = context.getString(R.string.label_duration_minutes, duration), fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                        if (endTime != null) {
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = timeFormat.format(endTime), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(text = dateFormat.format(endTime), fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 16.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.meeting_id_label), fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        Text(text = mtg.meetingId, fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = stringResource(R.string.icon_desc_copy), tint = Color.Gray, modifier = Modifier.size(18.dp).clickable {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText(context.getString(R.string.meeting_id_label), mtg.meetingId))
                        })
                    }
                }
                Divider(color = Color(0xFFE0E0E0))

                Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 16.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.label_host), fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp))
                    Text(text = "刘承龙", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(end = 8.dp))
                }
                Divider(color = Color(0xFFE0E0E0))

                Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(vertical = 16.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.meeting_phone_entry), fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        Text(text = stringResource(R.string.phone_number_china_mainland), fontSize = 14.sp, color = Color(0xFF1976D2))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                }
                Divider(color = Color(0xFFE0E0E0))

                Row(modifier = Modifier.fillMaxWidth().background(Color.White).clickable { }.padding(vertical = 16.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.label_application), fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        Text(text = stringResource(R.string.btn_add), fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
                Divider(color = Color(0xFFE0E0E0))

                Row(modifier = Modifier.fillMaxWidth().background(Color.White).clickable { }.padding(vertical = 16.dp, horizontal = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.meeting_materials), fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(start = 8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 8.dp)) {
                        Text(text = stringResource(R.string.msg_no_content_add), fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = { }, modifier = Modifier.weight(1f), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)) {
                Text(stringResource(R.string.btn_ai_hosting))
            }
            Button(onClick = { presenter.onEnterMeetingClicked() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))) {
                Text(stringResource(R.string.meeting_enter))
            }
        }

        errorMessage?.let { message -> LaunchedEffect(message) { errorMessage = null } }
    }
}
