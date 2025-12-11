package com.example.tencentmeeting.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.ScheduledMeetingDetailsContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.presenter.ScheduledMeetingDetailsPresenter
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScheduledMeetingDetailsPage(
    meetingId: String,
    onNavigateBack: () -> Unit = {},
    onNavigateToMeetingDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { ScheduledMeetingDetailsPresenter(dataRepository) }

    var currentMeeting by remember { mutableStateOf<Meeting?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : ScheduledMeetingDetailsContract.View {
            override fun showMeetingDetails(meeting: Meeting) {
                currentMeeting = meeting
            }

            override fun showError(message: String) {
                errorMessage = message
            }

            override fun navigateToMeetingDetails(meetingId: String) {
                onNavigateToMeetingDetails(meetingId)
            }
        }
    }

    LaunchedEffect(meetingId) {
        presenter.attachView(view)
        presenter.loadMeetingDetails(meetingId)
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFF5F5F5)
                    )
                )
            )
            .padding(top = 32.dp)
    ) {
        // 顶部栏
        TopBar(onNavigateBack = onNavigateBack)

        // 内容区域
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            currentMeeting?.let { mtg ->
                // 会议主题
                Text(
                    text = mtg.topic,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                // 时间信息
                TimeSection(meeting = mtg)

                Spacer(modifier = Modifier.height(16.dp))

                // 会议号
                InfoItemWithCopy(
                    label = "会议号",
                    value = mtg.meetingId,
                    context = context
                )

                Divider(color = Color(0xFFE0E0E0))

                // 发起人
                InfoItem(
                    label = "发起人",
                    value = "刘承龙"
                )

                Divider(color = Color(0xFFE0E0E0))

                // 电话入会
                InfoItem(
                    label = "电话入会",
                    value = "+86 (0)755 36550000 (中国大陆)",
                    showIcon = true
                )

                Divider(color = Color(0xFFE0E0E0))

                // 应用
                ClickableInfoItem(
                    label = "应用",
                    hint = "添加",
                    onClick = { /* 暂不处理 */ }
                )

                Divider(color = Color(0xFFE0E0E0))

                // 会议资料
                ClickableInfoItem(
                    label = "会议资料",
                    hint = "暂无内容，去添加",
                    onClick = { /* 暂不处理 */ }
                )
            }
        }

        // 底部按钮
        BottomButtons(
            onEnterMeeting = { presenter.onEnterMeetingClicked() }
        )

        // 错误提示
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                errorMessage = null
            }
        }
    }
}

@Composable
private fun TopBar(onNavigateBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "返回",
            modifier = Modifier
                .size(24.dp)
                .clickable { onNavigateBack() },
            tint = Color.Black
        )

        Text(
            text = "会议详情",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // 占位，保持标题居中
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun TimeSection(meeting: Meeting) {
    val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)
    val timeFormat = SimpleDateFormat("HH:mm", Locale.CHINA)

    val startDate = Date(meeting.startTime)
    val endTime = meeting.endTime?.let { Date(it) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 开始时间
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = timeFormat.format(startDate),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = dateFormat.format(startDate),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // 中间状态
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "待开始",
                    fontSize = 12.sp,
                    color = Color(0xFFFF9800)
                )
                if (endTime != null) {
                    val duration = (endTime.time - startDate.time) / (1000 * 60)
                    Text(
                        text = "${duration}分钟",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // 结束时间
            if (endTime != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = timeFormat.format(endTime),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = dateFormat.format(endTime),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    showIcon: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = if (showIcon) Color(0xFF1976D2) else Color.Gray
            )
            if (showIcon) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ClickableInfoItem(
    label: String,
    hint: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() }
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.Black
            )
            if (label == "电话入会") {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                text = hint,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun BottomButtons(onEnterMeeting: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // AI托管按钮
        OutlinedButton(
            onClick = { /* 暂不处理 */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text("AI托管")
        }

        // 进入会议按钮
        Button(
            onClick = onEnterMeeting,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2)
            )
        ) {
            Text("进入会议")
        }
    }
}

@Composable
private fun InfoItemWithCopy(
    label: String,
    value: String,
    context: Context
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "复制",
                tint = Color.Gray,
                modifier = Modifier
                    .size(18.dp)
                    .clickable {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("会议号", value)
                        clipboard.setPrimaryClip(clip)
                    }
            )
        }
    }
}
