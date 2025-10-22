package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.MeetingDetailsContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.MeetingDetailsPresenter

@Composable
fun MeetingDetailsPage(
    meetingId: String = "",
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MeetingDetailsPresenter(dataRepository) }

    var meetingTopic by remember { mutableStateOf("腾讯会议") }
    var meetingDuration by remember { mutableStateOf("00:00") }
    var participants by remember { mutableStateOf<List<User>>(emptyList()) }
    var micEnabled by remember { mutableStateOf(false) }
    var videoEnabled by remember { mutableStateOf(false) }
    var speakerEnabled by remember { mutableStateOf(true) }
    var danmuText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showMembersManage by remember { mutableStateOf(false) }

    // MVP View实现
    val view = remember {
        object : MeetingDetailsContract.View {
            override fun showMeetingInfo(topic: String, id: String) {
                meetingTopic = topic
            }

            override fun showParticipants(users: List<User>) {
                participants = users
            }

            override fun updateMicStatus(enabled: Boolean) {
                micEnabled = enabled
            }

            override fun updateVideoStatus(enabled: Boolean) {
                videoEnabled = enabled
            }

            override fun updateSpeakerStatus(enabled: Boolean) {
                speakerEnabled = enabled
            }

            override fun showMeetingDuration(duration: String) {
                meetingDuration = duration
            }

            override fun showLoading() {
                isLoading = true
            }

            override fun hideLoading() {
                isLoading = false
            }

            override fun showError(message: String) {
                errorMessage = message
            }

            override fun navigateBack() {
                onNavigateBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMeetingDetails(meetingId)
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2D3236))
    ) {
        // 主内容区域
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 顶部信息栏
            TopBar(
                meetingTopic = meetingTopic,
                meetingDuration = meetingDuration,
                speakerEnabled = speakerEnabled,
                onExitClick = { presenter.endMeeting() },
                onSpeakerClick = { presenter.toggleSpeaker() },
                onEndMeetingClick = { presenter.endMeeting() }
            )

            // 中间参会人区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (participants.isNotEmpty()) {
                    ParticipantView(
                        participant = participants.first(),
                        micEnabled = micEnabled
                    )
                }
            }

            // 底部功能区
            BottomFunctionBar(
                micEnabled = micEnabled,
                videoEnabled = videoEnabled,
                onMicClick = { presenter.toggleMic() },
                onVideoClick = { presenter.toggleVideo() },
                onShareScreenClick = { presenter.shareScreen() },
                onManageMemberClick = { showMembersManage = true }
            )
        }

        // 左下角弹幕输入区
        DanmuInputArea(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 120.dp),
            danmuText = danmuText,
            onDanmuTextChange = { danmuText = it },
            onSendClick = {
                presenter.sendDanmu(danmuText)
                danmuText = ""
            }
        )

        // 错误提示
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                kotlinx.coroutines.delay(2000)
                errorMessage = null
            }
        }

        // 成员管理页面
        if (showMembersManage) {
            MembersManagePage(
                onDismiss = { showMembersManage = false },
                micEnabled = micEnabled,
                videoEnabled = videoEnabled
            )
        }
    }
}

@Composable
private fun TopBar(
    meetingTopic: String,
    meetingDuration: String,
    speakerEnabled: Boolean,
    onExitClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    onEndMeetingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1F2227))
            .padding(start = 16.dp, end = 16.dp, top = 40.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 左侧声音按钮
        IconButton(onClick = onSpeakerClick) {
            Icon(
                imageVector = if (speakerEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                contentDescription = "声音",
                tint = Color.White
            )
        }

        // 中间标题和时间
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = meetingTopic,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = meetingDuration,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // 右侧结束按钮
        TextButton(
            onClick = onEndMeetingClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = Color.Red
            )
        ) {
            Text(
                text = "结束",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ParticipantView(
    participant: User,
    micEnabled: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            // 圆形头像
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

            // 麦克风状态图标
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
                        contentDescription = "静音",
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

@Composable
private fun DanmuInputArea(
    modifier: Modifier = Modifier,
    danmuText: String,
    onDanmuTextChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Row(
        modifier = modifier
            .width(280.dp)
            .height(48.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF3C4148))
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "聊天",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Icon(
            imageVector = Icons.Default.EmojiEmotions,
            contentDescription = "表情",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = "说点什么...",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BottomFunctionBar(
    micEnabled: Boolean,
    videoEnabled: Boolean,
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
            text = if (micEnabled) "静音" else "解除静音",
            iconTint = if (micEnabled) Color.White else Color.Red,
            onClick = onMicClick
        )

        FunctionButton(
            icon = if (videoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
            text = if (videoEnabled) "关闭视频" else "开启视频",
            iconTint = if (videoEnabled) Color.White else Color.Red,
            onClick = onVideoClick
        )

        FunctionButton(
            icon = Icons.Default.ScreenShare,
            text = "共享屏幕",
            iconTint = Color(0xFF4CAF50),
            onClick = onShareScreenClick
        )

        FunctionButton(
            icon = Icons.Default.People,
            text = "管理成员(1)",
            iconTint = Color.White,
            onClick = onManageMemberClick
        )
    }
}

@Composable
private fun FunctionButton(
    icon: ImageVector,
    text: String,
    iconTint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = text,
            fontSize = 11.sp,
            color = Color.White
        )
    }
}
