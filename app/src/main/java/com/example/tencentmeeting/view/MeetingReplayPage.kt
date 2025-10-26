package com.example.tencentmeeting.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.MeetingReplayContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.presenter.MeetingReplayPresenter
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingReplayPage(
    meetingId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MeetingReplayPresenter(dataRepository) }

    var meeting by remember { mutableStateOf<Meeting?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableStateOf(1.0f) }

    // MVP View实现
    val view = remember {
        object : MeetingReplayContract.View {
            override fun showMeetingInfo(meetingInfo: Meeting) {
                meeting = meetingInfo
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
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMeetingInfo(meetingId)
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = meeting?.topic ?: "会议回放",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF1F2227)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF2D3236))
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (meeting != null) {
                // 计算会议时长
                val duration = if (meeting!!.endTime != null) {
                    val durationMillis = meeting!!.endTime!! - meeting!!.startTime
                    val hours = durationMillis / (1000 * 60 * 60)
                    val minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60)
                    val seconds = (durationMillis % (1000 * 60)) / 1000

                    when {
                        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
                        else -> String.format("%d:%02d", minutes, seconds)
                    }
                } else {
                    "00:00"
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 视频播放区域
                VideoPlaybackArea(
                    isPlaying = isPlaying,
                    onPlayPauseClick = { isPlaying = !isPlaying },
                    duration = duration
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 播放控制栏
                PlaybackControls(
                    isPlaying = isPlaying,
                    onPlayPauseClick = { isPlaying = !isPlaying },
                    currentTime = "00:00",
                    totalTime = duration,
                    playbackSpeed = playbackSpeed,
                    onSpeedClick = {
                        playbackSpeed = when (playbackSpeed) {
                            1.0f -> 1.5f
                            1.5f -> 2.0f
                            else -> 1.0f
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 会议信息卡片
                MeetingInfoCard(meeting = meeting!!)
            }

            // 错误提示
            errorMessage?.let { message ->
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(3000)
                    errorMessage = null
                }
            }
        }
    }
}

@Composable
private fun VideoPlaybackArea(
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    duration: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 播放按钮
            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "暂停" else "播放",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            // 提示文字
            Text(
                text = "会议回放",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "时长: $duration",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun PlaybackControls(
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    currentTime: String,
    totalTime: String,
    playbackSpeed: Float,
    onSpeedClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // 进度条
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = currentTime,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            LinearProgressIndicator(
                progress = { 0f },
                modifier = Modifier.weight(1f),
                color = Color(0xFF1976D2),
                trackColor = Color.White.copy(alpha = 0.3f),
            )

            Text(
                text = totalTime,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 控制按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 播放/暂停按钮
            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "暂停" else "播放",
                    tint = Color.White
                )
            }

            // 倍速按钮
            OutlinedButton(
                onClick = onSpeedClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.5f))
                )
            ) {
                Text(
                    text = "${playbackSpeed}x",
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun MeetingInfoCard(meeting: Meeting) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3C4148)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "会议信息",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Divider(color = Color.White.copy(alpha = 0.2f))

            InfoRow(label = "会议号", value = meeting.meetingId)

            InfoRow(
                label = "开始时间",
                value = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(Date(meeting.startTime))
            )

            if (meeting.endTime != null) {
                val durationMillis = meeting.endTime!! - meeting.startTime
                val hours = durationMillis / (1000 * 60 * 60)
                val minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60)
                val durationText = when {
                    hours > 0 && minutes > 0 -> "${hours}小时${minutes}分钟"
                    hours > 0 -> "${hours}小时"
                    minutes > 0 -> "${minutes}分钟"
                    else -> "少于1分钟"
                }
                InfoRow(label = "会议时长", value = durationText)
            }

            InfoRow(label = "参会人数", value = "${meeting.participantIds.size}人")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}
