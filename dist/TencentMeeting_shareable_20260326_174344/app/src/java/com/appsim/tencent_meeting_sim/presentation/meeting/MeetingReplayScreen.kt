package com.appsim.tencent_meeting_sim.presentation.meeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingReplayContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingReplayPresenter
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingReplayScreen(meetingId: String, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MeetingReplayPresenter(dataRepository) }
    var meeting by remember { mutableStateOf<Meeting?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableStateOf(1.0f) }

    val view = remember {
        object : MeetingReplayContract.View {
            override fun showMeetingInfo(meetingInfo: Meeting) { meeting = meetingInfo }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadMeetingInfo(meetingId) }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = meeting?.topic ?: stringResource(R.string.meeting_replay), fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color.White) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.icon_desc_back), tint = Color.White) } },
                actions = {
                    IconButton(onClick = { }) { Icon(imageVector = Icons.Default.Download, contentDescription = "Download", tint = Color.White) }
                    IconButton(onClick = { }) { Icon(imageVector = Icons.Default.Share, contentDescription = stringResource(R.string.icon_desc_share), tint = Color.White) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF1F2227))
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(Color(0xFF2D3236)).verticalScroll(rememberScrollState())) {
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f).background(Color.Black), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = "Video", tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(80.dp))
            }

            Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF3C4148)).padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "00:00", fontSize = 14.sp, color = Color.White)
                Slider(value = 0f, onValueChange = { }, modifier = Modifier.weight(1f).padding(horizontal = 16.dp), colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color(0xFF1976D2), inactiveTrackColor = Color.Gray))
                Text(text = "00:00", fontSize = 14.sp, color = Color.White)
            }

            Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF3C4148)).padding(horizontal = 16.dp, vertical = 12.dp), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { }) { Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = stringResource(R.string.btn_previous), tint = Color.White, modifier = Modifier.size(32.dp)) }
                FloatingActionButton(onClick = { isPlaying = !isPlaying }, containerColor = Color(0xFF1976D2), modifier = Modifier.size(56.dp)) {
                    Icon(imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = if (isPlaying) stringResource(R.string.btn_pause) else stringResource(R.string.btn_play), tint = Color.White)
                }
                IconButton(onClick = { }) { Icon(imageVector = Icons.Default.SkipNext, contentDescription = stringResource(R.string.btn_next), tint = Color.White, modifier = Modifier.size(32.dp)) }
                OutlinedButton(onClick = { playbackSpeed = when (playbackSpeed) { 1.0f -> 1.5f; 1.5f -> 2.0f; else -> 1.0f } }, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White), border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.5f)))) {
                    Text(text = "${playbackSpeed}x", fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            meeting?.let { mtg ->
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF3C4148)), shape = RoundedCornerShape(12.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(text = stringResource(R.string.label_meeting_info), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Divider(color = Color.White.copy(alpha = 0.2f))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = stringResource(R.string.meeting_id_label), fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                            Text(text = mtg.meetingId, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = stringResource(R.string.label_start_time), fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                            Text(text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(mtg.startTime)), fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                        if (mtg.endTime != null) {
                            val durationMillis = mtg.endTime!! - mtg.startTime
                            val hours = durationMillis / (1000 * 60 * 60)
                            val minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60)
                            val durationText = when {
                                hours > 0 && minutes > 0 -> "$hours${context.getString(R.string.label_hours)}$minutes${context.getString(R.string.label_minutes_unit)}"
                                hours > 0 -> "$hours${context.getString(R.string.label_hours)}"
                                minutes > 0 -> "$minutes${context.getString(R.string.label_minutes_unit)}"
                                else -> context.getString(R.string.label_less_than_minute)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = stringResource(R.string.meeting_duration), fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                                Text(text = durationText, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = stringResource(R.string.label_participants_count), fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                            Text(text = "${mtg.participantIds.size}${context.getString(R.string.label_person_unit)}", fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    errorMessage?.let { message -> LaunchedEffect(message) { errorMessage = null } }
}
