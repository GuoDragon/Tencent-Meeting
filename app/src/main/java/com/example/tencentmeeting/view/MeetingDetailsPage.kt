package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
    initialMicEnabled: Boolean = false,
    initialVideoEnabled: Boolean = false,
    initialSpeakerEnabled: Boolean = true,
    onNavigateBack: () -> Unit,
    onNavigateToChatPage: () -> Unit = {}
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MeetingDetailsPresenter(dataRepository, initialMicEnabled, initialVideoEnabled, initialSpeakerEnabled) }

    var meetingTopic by remember { mutableStateOf("腾讯会议") }
    var meetingDuration by remember { mutableStateOf("00:00") }
    var participants by remember { mutableStateOf<List<User>>(emptyList()) }
    var micEnabled by remember { mutableStateOf(initialMicEnabled) }
    var videoEnabled by remember { mutableStateOf(initialVideoEnabled) }
    var speakerEnabled by remember { mutableStateOf(initialSpeakerEnabled) }
    var isScreenSharing by remember { mutableStateOf(false) }
    var isRecording by remember { mutableStateOf(false) }
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

            override fun updateScreenShareStatus(isSharing: Boolean) {
                isScreenSharing = isSharing
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
                isRecording = isRecording,
                onExitClick = { presenter.endMeeting() },
                onSpeakerClick = { presenter.toggleSpeaker() },
                onRecordingClick = { isRecording = !isRecording },
                onEndMeetingClick = { presenter.endMeeting() }
            )

            // 中间内容区域（参会人或屏幕共享）
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                if (isScreenSharing) {
                    // 屏幕共享视图
                    ScreenShareView()
                } else {
                    // 参会人视图
                    if (participants.isNotEmpty()) {
                        ParticipantView(
                            participant = participants.first(),
                            micEnabled = micEnabled
                        )
                    }
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

        // 左下角聊天入口
        DanmuInputArea(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 120.dp),
            onClick = onNavigateToChatPage
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
    isRecording: Boolean,
    onExitClick: () -> Unit,
    onSpeakerClick: () -> Unit,
    onRecordingClick: () -> Unit,
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
        // 左侧按钮组（扬声器和录制）
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 扬声器按钮
            IconButton(onClick = onSpeakerClick) {
                Icon(
                    imageVector = if (speakerEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = "声音",
                    tint = Color.White
                )
            }

            // 录制按钮
            IconButton(onClick = onRecordingClick) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.FiberManualRecord else Icons.Default.RadioButtonUnchecked,
                    contentDescription = if (isRecording) "停止录制" else "开始录制",
                    tint = if (isRecording) Color.Red else Color.Gray
                )
            }
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
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .width(280.dp)
            .height(48.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(Color(0xFF3C4148))
            .padding(horizontal = 12.dp)
            .clickable { onClick() },
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

/**
 * 屏幕共享视图组件
 * 模拟显示手机桌面屏幕
 */
@Composable
private fun ScreenShareView() {
    // 手机屏幕容器（带边框）
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(9f / 19f)  // 手机屏幕比例
            .clip(RoundedCornerShape(32.dp))
            .border(
                width = 8.dp,
                color = Color(0xFF2C2C2E),
                shape = RoundedCornerShape(32.dp)
            )
            .background(Color.Black)
    ) {
        // 屏幕内容
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5D7A9E),  // 蓝灰色
                            Color(0xFF2C3E50),  // 深蓝灰
                            Color(0xFF1A1A2E)   // 深色
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 顶部状态栏
                ScreenStatusBar()

                Spacer(modifier = Modifier.weight(1f))

                // 底部应用图标区域
                ScreenAppIcons()

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

/**
 * 模拟手机屏幕顶部状态栏
 */
@Composable
private fun ScreenStatusBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 24.dp)
    ) {
        // 时间
        Text(
            text = "6:59",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f),
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 日期
        Text(
            text = "Thu, Oct 23",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f),
            fontWeight = FontWeight.Light
        )
    }
}

/**
 * 模拟手机屏幕应用图标
 */
@Composable
private fun ScreenAppIcons() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 第一行应用图标
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppIcon(icon = Icons.Default.Email, label = "Gmail")
            AppIcon(icon = Icons.Default.PhotoLibrary, label = "Photos")
            AppIcon(icon = Icons.Default.PlayCircle, label = "YouTube")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 第二行应用图标
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AppIcon(icon = Icons.Default.Phone, label = "Phone")
            AppIcon(icon = Icons.Default.Message, label = "Messages")
            Spacer(modifier = Modifier.width(64.dp)) // 空白占位
            AppIcon(icon = Icons.Default.Email, label = "Gmail")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 搜索栏
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF5F6368),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Voice",
                    tint = Color(0xFF4285F4),
                    modifier = Modifier.size(24.dp)
                )

                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Camera",
                    tint = Color(0xFF4285F4),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 导航栏指示器
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.6f))
        )
    }
}

/**
 * 单个应用图标组件
 */
@Composable
private fun AppIcon(
    icon: ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(72.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF4285F4),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )
    }
}
