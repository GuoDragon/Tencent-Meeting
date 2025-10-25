package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.MeContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.MePresenter

@Composable
fun MePage() {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val topSpacing = screenHeight * 0.1f // 页面高度的十分之一
    
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MePresenter(dataRepository) }
    
    var currentUser by remember { mutableStateOf<User?>(null) }
    var historyMeetings by remember { mutableStateOf<List<Meeting>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showEmptyState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val view = remember {
        object : MeContract.View {
            override fun showUserInfo(user: User) {
                currentUser = user
            }
            
            override fun showHistoryMeetings(meetings: List<Meeting>) {
                historyMeetings = meetings
                showEmptyState = false
            }
            
            override fun showEmptyHistory() {
                historyMeetings = emptyList()
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
        }
    }
    
    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadUserInfo()
        presenter.loadHistoryMeetings()
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
            .verticalScroll(rememberScrollState())
    ) {
        // 向下移动页面十分之一的空间
        Spacer(modifier = Modifier.height(topSpacing))

        // 用户信息卡片
        UserInfoCard(user = currentUser)

        // 历史会议列表
        HistoryMeetingsSection(
            meetings = historyMeetings,
            showEmptyState = showEmptyState,
            isLoading = isLoading
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
private fun UserInfoCard(user: User?) {
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
                        text = user?.phone ?: "未设置手机号",
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

                // 设置图标
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "设置",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { }
                )
            }
        }
    }
}

@Composable
private fun HistoryMeetingsSection(
    meetings: List<Meeting>,
    showEmptyState: Boolean,
    isLoading: Boolean
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
                text = "历史会议",
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
                EmptyHistoryState()
            }
            else -> {
                Column {
                    meetings.forEach { meeting ->
                        HistoryMeetingItem(meeting = meeting)
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = "暂无历史会议",
            tint = Color.Gray,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "暂无历史会议",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun HistoryMeetingItem(meeting: Meeting) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { },
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
                contentDescription = "历史会议",
                tint = Color.Gray,
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
                text = "已结束",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}