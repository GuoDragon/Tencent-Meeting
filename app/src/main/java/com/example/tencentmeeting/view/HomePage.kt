package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.HomeContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.HomePresenter

@Composable
fun HomePage(
    onNavigateToScheduledMeeting: () -> Unit = {},
    onNavigateToJoinMeeting: () -> Unit = {},
    onNavigateToQuickMeeting: () -> Unit = {},
    onNavigateToMeetingDetails: (String) -> Unit = {},
    onNavigateToScheduledMeetingDetails: (String) -> Unit = {},
    onNavigateToHistoryMeetings: () -> Unit = {},
    onNavigateToShareScreen: () -> Unit = {}
) {
    val context = LocalContext.current

    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { HomePresenter(dataRepository) }

    var currentUser by remember { mutableStateOf<User?>(null) }
    var currentMeetings by remember { mutableStateOf<List<Meeting>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showEmptyState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : HomeContract.View {
            override fun showMeetings(meetings: List<Meeting>) {
                currentMeetings = meetings
                showEmptyState = false
            }

            override fun showEmptyMeetings() {
                currentMeetings = emptyList()
                showEmptyState = true
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

            override fun navigateToJoinMeeting() {
                onNavigateToJoinMeeting()
            }

            override fun navigateToQuickMeeting() {
                onNavigateToQuickMeeting()
            }

            override fun navigateToScheduledMeeting() {
                onNavigateToScheduledMeeting()
            }

            override fun showUserInfo(user: User) {
                currentUser = user
            }

            override fun showHistoryMeetings(meetings: List<Meeting>) {
                // 不再在HomePage显示历史会议，导航到独立页面
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadCurrentUser()
        presenter.loadMeetings()
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
    ) {
        // 顶部间距，让个人信息区域往下移动一点
        Spacer(modifier = Modifier.height(24.dp))

        // 用户信息区域
        currentUser?.let { user ->
            UserInfoSection(user = user)
        }

        // 用户信息和功能按钮之间的间距（缩小间距，让功能区往上移动）
        Spacer(modifier = Modifier.height(16.dp))

        // 功能按钮区域
        FunctionButtonsSection(
            onJoinMeetingClick = { presenter.onJoinMeetingClicked() },
            onQuickMeetingClick = { presenter.onQuickMeetingClicked() },
            onScheduledMeetingClick = { presenter.onScheduledMeetingClicked() },
            onShareScreenClick = onNavigateToShareScreen
        )

        // 会议列表区域
        MeetingsSection(
            meetings = currentMeetings,
            showEmptyState = showEmptyState,
            isLoading = isLoading,
            onNavigateToMeetingDetails = onNavigateToMeetingDetails,
            onNavigateToScheduledMeetingDetails = onNavigateToScheduledMeetingDetails,
            onHistoryMeetingsClick = onNavigateToHistoryMeetings
        )

        // 错误提示
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                // 可以添加Snackbar显示错误
                errorMessage = null
            }
        }
    }
}

@Composable
private fun UserInfoSection(user: User) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 顶部用户基本信息
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 头像
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1976D2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "刘",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 用户信息
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "刘承龙",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = user.phone ?: "未设置手机号",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "liuchenglong@example.com",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
private fun FunctionButtonsSection(
    onJoinMeetingClick: () -> Unit,
    onQuickMeetingClick: () -> Unit,
    onScheduledMeetingClick: () -> Unit,
    onShareScreenClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FunctionButton(
            icon = Icons.Default.Add,
            text = "加入会议",
            onClick = onJoinMeetingClick
        )
        FunctionButton(
            icon = Icons.Default.Bolt,
            text = "快速会议",
            onClick = onQuickMeetingClick
        )
        FunctionButton(
            icon = Icons.Default.Schedule,
            text = "预定会议",
            onClick = onScheduledMeetingClick
        )
        FunctionButton(
            icon = Icons.Default.ScreenShare,
            text = "共享屏幕",
            onClick = onShareScreenClick
        )
    }
}

@Composable
private fun FunctionButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Card(
            modifier = Modifier
                .size(64.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MeetingsSection(
    meetings: List<Meeting>,
    showEmptyState: Boolean,
    isLoading: Boolean,
    onNavigateToMeetingDetails: (String) -> Unit,
    onNavigateToScheduledMeetingDetails: (String) -> Unit,
    onHistoryMeetingsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "会议列表",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // 历史会议按钮
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFF0F0F0))
                    .clickable { onHistoryMeetingsClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "历史会议",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "查看历史会议",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1976D2))
                }
            }
            showEmptyState -> {
                EmptyMeetingsState()
            }
            else -> {
                LazyColumn {
                    items(meetings) { meeting ->
                        MeetingItem(
                            meeting = meeting,
                            onNavigateToMeetingDetails = onNavigateToMeetingDetails,
                            onNavigateToScheduledMeetingDetails = onNavigateToScheduledMeetingDetails
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyMeetingsState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CloudOff,
            contentDescription = "暂无会议",
            tint = Color.Gray,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "暂无会议",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun MeetingItem(
    meeting: Meeting,
    onNavigateToMeetingDetails: (String) -> Unit,
    onNavigateToScheduledMeetingDetails: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                // ONGOING状态 -> 进入会议详情
                // UPCOMING状态 -> 显示预定会议详情
                when (meeting.status.name) {
                    "ONGOING" -> onNavigateToMeetingDetails(meeting.meetingId)
                    "UPCOMING" -> onNavigateToScheduledMeetingDetails(meeting.meetingId)
                }
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.VideoCall,
                contentDescription = "会议",
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meeting.topic,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "会议号: ${meeting.meetingId}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Text(
                text = when (meeting.status.name) {
                    "ONGOING" -> "进行中"
                    "UPCOMING" -> "待开始"
                    else -> "已结束"
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