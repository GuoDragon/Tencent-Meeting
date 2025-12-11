package com.appsim.tencent_meeting_sim.presentation.meeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingDetailsContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingDetailsPresenter
import com.appsim.tencent_meeting_sim.presentation.meeting.components.ScreenShareView
import com.appsim.tencent_meeting_sim.presentation.meeting.components.ParticipantView
import com.appsim.tencent_meeting_sim.presentation.meeting.components.DanmuInputArea
import com.appsim.tencent_meeting_sim.presentation.meeting.components.BottomFunctionBar

@Composable
fun MeetingDetailsScreen(
    meetingId: String = "",
    initialMicEnabled: Boolean = false,
    initialVideoEnabled: Boolean = false,
    initialSpeakerEnabled: Boolean = true,
    initialRecordingEnabled: Boolean = false,
    initialScreenSharing: Boolean = false,
    onNavigateBack: () -> Unit,
    onNavigateToChatPage: () -> Unit = {}
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MeetingDetailsPresenter(dataRepository, initialMicEnabled, initialVideoEnabled, initialSpeakerEnabled) }

    var meetingTopic by remember { mutableStateOf(context.getString(R.string.meeting_default_name)) }
    var meetingDuration by remember { mutableStateOf("00:00") }
    var participants by remember { mutableStateOf<List<User>>(emptyList()) }
    var micEnabled by remember { mutableStateOf(initialMicEnabled) }
    var videoEnabled by remember { mutableStateOf(initialVideoEnabled) }
    var speakerEnabled by remember { mutableStateOf(initialSpeakerEnabled) }
    var isScreenSharing by remember { mutableStateOf(initialScreenSharing) }
    var isRecording by remember { mutableStateOf(initialRecordingEnabled) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showMembersManage by remember { mutableStateOf(false) }
    var isHandRaised by remember { mutableStateOf(false) }
    var raisedHandUsers by remember { mutableStateOf<List<User>>(emptyList()) }

    // MVP View实现
    val view = remember {
        object : MeetingDetailsContract.View {
            override fun showMeetingInfo(topic: String, id: String) { meetingTopic = topic }
            override fun showParticipants(users: List<User>) { participants = users }
            override fun updateMicStatus(enabled: Boolean) { micEnabled = enabled }
            override fun updateVideoStatus(enabled: Boolean) { videoEnabled = enabled }
            override fun updateSpeakerStatus(enabled: Boolean) { speakerEnabled = enabled }
            override fun updateScreenShareStatus(isSharing: Boolean) { isScreenSharing = isSharing }
            override fun showMeetingDuration(duration: String) { meetingDuration = duration }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun navigateBack() { onNavigateBack() }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMeetingDetails(meetingId)
    }

    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2D3236))) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 顶部信息栏
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF1F2227))
                    .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = { presenter.toggleSpeaker() }) {
                        Icon(
                            imageVector = if (speakerEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = stringResource(R.string.icon_desc_volume),
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { isRecording = !isRecording }) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.FiberManualRecord else Icons.Default.RadioButtonUnchecked,
                            contentDescription = if (isRecording) stringResource(R.string.device_recording_stop) else stringResource(R.string.device_recording_start),
                            tint = if (isRecording) Color.Red else Color.Gray
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = meetingTopic, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Text(text = meetingDuration, fontSize = 12.sp, color = Color.Gray)
                }
                TextButton(
                    onClick = { presenter.endMeeting() },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                ) {
                    Text(text = stringResource(R.string.meeting_end), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            // 中间内容区域
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                if (isScreenSharing) {
                    ScreenShareView()
                } else {
                    if (participants.isNotEmpty()) {
                        ParticipantView(participant = participants.first(), micEnabled = micEnabled)
                    }
                }
            }

            // 底部功能区
            BottomFunctionBar(
                micEnabled = micEnabled,
                videoEnabled = videoEnabled,
                isScreenSharing = isScreenSharing,
                participantCount = participants.size,
                onMicClick = { presenter.toggleMic() },
                onVideoClick = { presenter.toggleVideo() },
                onShareScreenClick = { presenter.shareScreen() },
                onManageMemberClick = { showMembersManage = true }
            )
        }

        // 左下角聊天入口和举手功能
        DanmuInputArea(
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 120.dp),
            onClick = onNavigateToChatPage,
            isHandRaised = isHandRaised,
            onHandRaiseClick = { isHandRaised = !isHandRaised }
        )

        // 主持人视角：显示举手提示和解除静音按钮
        if (raisedHandUsers.isNotEmpty()) {
            Box(modifier = Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 180.dp)) {
                raisedHandUsers.forEach { user ->
                    Card(
                        modifier = Modifier.padding(bottom = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF3C4148)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PanTool,
                                contentDescription = stringResource(R.string.icon_desc_hand_raise),
                                tint = Color(0xFFFFA726),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(text = context.getString(R.string.action_hand_raise), fontSize = 13.sp, color = Color.White)
                            Button(
                                onClick = {
                                    presenter.toggleMic()
                                    raisedHandUsers = raisedHandUsers - user
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                modifier = Modifier.height(28.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = stringResource(R.string.device_mic_unmute), fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // 错误提示
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                kotlinx.coroutines.delay(2000)
                errorMessage = null
            }
        }

        // 成员管理页面
        if (showMembersManage) {
            MembersManageScreen(
                onDismiss = { showMembersManage = false },
                micEnabled = micEnabled,
                videoEnabled = videoEnabled
            )
        }
    }
}
