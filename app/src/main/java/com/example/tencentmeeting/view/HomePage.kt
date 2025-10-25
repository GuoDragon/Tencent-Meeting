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
    onNavigateToMeetingDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val topSpacing = screenHeight * 0.1f // 页面高度的十分之一

    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { HomePresenter(dataRepository) }
    
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
        }
    }
    
    LaunchedEffect(Unit) {
        presenter.attachView(view)
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
            .background(Color(0xFFF5F5F5))
    ) {
        // 向下移动页面十分之一的空间
        Spacer(modifier = Modifier.height(topSpacing))
        
        // 功能按钮区域
        FunctionButtonsSection(
            onJoinMeetingClick = { presenter.onJoinMeetingClicked() },
            onQuickMeetingClick = { presenter.onQuickMeetingClicked() },
            onScheduledMeetingClick = { presenter.onScheduledMeetingClicked() }
        )
        
        // 会议列表区域
        MeetingsSection(
            meetings = currentMeetings,
            showEmptyState = showEmptyState,
            isLoading = isLoading,
            onNavigateToMeetingDetails = onNavigateToMeetingDetails
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
private fun FunctionButtonsSection(
    onJoinMeetingClick: () -> Unit,
    onQuickMeetingClick: () -> Unit,
    onScheduledMeetingClick: () -> Unit
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
    onNavigateToMeetingDetails: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "会议列表",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
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
                            onNavigateToMeetingDetails = onNavigateToMeetingDetails
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
    onNavigateToMeetingDetails: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                // Only navigate to meeting details if status is ONGOING
                if (meeting.status.name == "ONGOING") {
                    onNavigateToMeetingDetails(meeting.meetingId)
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