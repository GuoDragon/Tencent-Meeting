package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.MeContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.MePresenter

@Composable
fun MePage(
    onMeetingClick: (String) -> Unit = {},
    onPersonalMeetingRoomClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MePresenter(dataRepository) }

    var currentUser by remember { mutableStateOf<User?>(null) }

    val view = remember {
        object : MeContract.View {
            override fun showUserInfo(user: User) {
                currentUser = user
            }

            override fun showHistoryMeetings(meetings: List<Meeting>) {
                // 不再使用
            }

            override fun showEmptyHistory() {
                // 不再使用
            }

            override fun showLoading() {
                // 不再使用
            }

            override fun hideLoading() {
                // 不再使用
            }

            override fun showError(message: String) {
                // 不再使用
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadUserInfo()
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
            .verticalScroll(rememberScrollState())
    ) {
        // 顶部间距，与HomePage保持一致
        Spacer(modifier = Modifier.height(24.dp))

        // 用户信息卡片
        UserInfoCard(user = currentUser)

        Spacer(modifier = Modifier.height(16.dp))

        // 功能网格区域
        FeatureGridSection(
            onRecordingClick = { onMeetingClick("") },
            onPersonalMeetingRoomClick = onPersonalMeetingRoomClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 设置列表区域
        SettingsListSection()

        Spacer(modifier = Modifier.height(24.dp))

        // 退出登录按钮
        LogoutButton()

        Spacer(modifier = Modifier.height(32.dp))
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
            }
        }
    }
}

@Composable
private fun FeatureGridSection(
    onRecordingClick: () -> Unit,
    onPersonalMeetingRoomClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 第一行：个人会议室、录制、我的笔记
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FeatureGridItem(
                    icon = Icons.Default.Home,
                    label = "个人会议室",
                    onClick = onPersonalMeetingRoomClick,
                    modifier = Modifier.weight(1f)
                )
                FeatureGridItem(
                    icon = Icons.Default.Videocam,
                    label = "录制",
                    onClick = onRecordingClick,
                    modifier = Modifier.weight(1f)
                )
                FeatureGridItem(
                    icon = Icons.Default.Note,
                    label = "我的笔记",
                    onClick = { /* 只显示，不需要功能 */ },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 第二行：AI 助手、订单与服务、控制Rooms
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FeatureGridItem(
                    icon = Icons.Default.SmartToy,
                    label = "AI 助手",
                    onClick = { /* 只显示，不需要功能 */ },
                    modifier = Modifier.weight(1f)
                )
                FeatureGridItem(
                    icon = Icons.Default.ShoppingCart,
                    label = "订单与服务",
                    onClick = { /* 只显示，不需要功能 */ },
                    modifier = Modifier.weight(1f)
                )
                FeatureGridItem(
                    icon = Icons.Default.MeetingRoom,
                    label = "控制Rooms",
                    onClick = { /* 只显示，不需要功能 */ },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FeatureGridItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.Black
        )
    }
}

@Composable
private fun SettingsListSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        SettingsListItem(
            icon = Icons.Default.Stars,
            text = "积分中心",
            onClick = { /* 只显示，不需要功能 */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsListItem(
            icon = Icons.Default.Security,
            text = "账号与安全",
            onClick = { /* 只显示，不需要功能 */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsListItem(
            icon = Icons.Default.Settings,
            text = "设置",
            onClick = { /* 只显示，不需要功能 */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsListItem(
            icon = Icons.Default.Lock,
            text = "隐私",
            onClick = { /* 只显示，不需要功能 */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsListItem(
            icon = Icons.Default.Help,
            text = "帮助与客服",
            onClick = { /* 只显示，不需要功能 */ }
        )

        Spacer(modifier = Modifier.height(8.dp))

        SettingsListItem(
            icon = Icons.Default.Info,
            text = "关于我们",
            onClick = { /* 只显示，不需要功能 */ }
        )
    }
}

@Composable
private fun SettingsListItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = text,
                fontSize = 15.sp,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "进入",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun LogoutButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { /* 只显示，不需要功能 */ },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "退出登录",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFFE53935)
        )
    }
}