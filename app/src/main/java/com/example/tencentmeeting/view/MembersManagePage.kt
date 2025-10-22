package com.example.tencentmeeting.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tencentmeeting.contract.MembersManageContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.MembersManagePresenter

@Composable
fun MembersManagePage(
    onDismiss: () -> Unit,
    micEnabled: Boolean = false,
    videoEnabled: Boolean = false
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MembersManagePresenter(dataRepository) }

    var members by remember { mutableStateOf<List<User>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf(0) } // 0: 会议中, 1: 未入会
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var allMicsMuted by remember { mutableStateOf(false) }

    // MVP View实现
    val view = remember {
        object : MembersManageContract.View {
            override fun showMembers(membersList: List<User>) {
                members = membersList
            }

            override fun updateMemberMicStatus(userId: String, enabled: Boolean) {
                // 更新成员麦克风状态
            }

            override fun updateMemberVideoStatus(userId: String, enabled: Boolean) {
                // 更新成员摄像头状态
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

            override fun showMuteAllSuccess() {
                errorMessage = "已全员静音"
            }

            override fun showUnmuteAllSuccess() {
                errorMessage = "已解除全员静音"
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMembers()
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.85f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(Color.White)
            ) {
                // 顶部信息栏
                TopBar(onDismiss = onDismiss)

                // 搜索框和邀请按钮
                SearchAndInviteBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = {
                        searchQuery = it
                        presenter.searchMember(it)
                    },
                    onInviteClick = { presenter.inviteMember() }
                )

                // Tab切换
                TabRow(
                    selectedTab = selectedTab,
                    memberCount = members.size,
                    onTabSelected = { selectedTab = it }
                )

                // 成员列表
                if (selectedTab == 0) {
                    MembersList(
                        members = members,
                        micEnabled = micEnabled,
                        videoEnabled = videoEnabled,
                        allMicsMuted = allMicsMuted,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    EmptyMembersList(modifier = Modifier.weight(1f))
                }

                // 底部按钮
                BottomButtons(
                    onMuteAllClick = {
                        allMicsMuted = true
                        presenter.muteAll()
                    },
                    onUnmuteAllClick = {
                        allMicsMuted = false
                        presenter.unmuteAll()
                    }
                )
            }

            // 错误提示
            errorMessage?.let { message ->
                LaunchedEffect(message) {
                    kotlinx.coroutines.delay(2000)
                    errorMessage = null
                }
            }
        }
    }
}

@Composable
private fun TopBar(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 左侧占位（设置按钮不做）
        Spacer(modifier = Modifier.width(48.dp))

        // 中间标题
        Text(
            text = "管理成员",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        // 右侧关闭按钮
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "关闭",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun SearchAndInviteBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onInviteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 搜索框
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = "搜索成员",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = Color.Gray
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            ),
            modifier = Modifier.weight(1f)
        )

        // 邀请按钮
        IconButton(
            onClick = onInviteClick,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = "邀请",
                tint = Color.Black
            )
        }
    }
}

@Composable
private fun TabRow(
    selectedTab: Int,
    memberCount: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 会议中Tab
        TabButton(
            text = "会议中($memberCount)",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            modifier = Modifier.weight(1f)
        )

        // 未入会Tab
        TabButton(
            text = "未入会",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF1976D2) else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
private fun MembersList(
    members: List<User>,
    micEnabled: Boolean,
    videoEnabled: Boolean,
    allMicsMuted: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        items(members) { member ->
            MemberItem(
                member = member,
                micEnabled = micEnabled,
                videoEnabled = videoEnabled,
                allMicsMuted = allMicsMuted
            )
        }
    }
}

@Composable
private fun MemberItem(
    member: User,
    micEnabled: Boolean,
    videoEnabled: Boolean,
    allMicsMuted: Boolean
) {
    // 判断麦克风是否应该显示为静音状态
    val shouldShowMicMuted = allMicsMuted || !micEnabled

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF4A5568)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.username.take(1),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 姓名和标签
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = member.username,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = "(主持人，我)",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // 麦克风图标
        Icon(
            imageVector = if (shouldShowMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
            contentDescription = if (shouldShowMicMuted) "麦克风关闭" else "麦克风开启",
            tint = if (shouldShowMicMuted) Color.Red else Color.Gray,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // 摄像头图标
        Icon(
            imageVector = if (videoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
            contentDescription = if (videoEnabled) "摄像头开启" else "摄像头关闭",
            tint = if (videoEnabled) Color.Gray else Color.Red,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun EmptyMembersList(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "暂无未入会成员",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun BottomButtons(
    onMuteAllClick: () -> Unit,
    onUnmuteAllClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 全体静音按钮
        OutlinedButton(
            onClick = onMuteAllClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "全体静音",
                fontSize = 14.sp
            )
        }

        // 解除全体静音按钮
        OutlinedButton(
            onClick = onUnmuteAllClick,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "解除全体静音",
                fontSize = 14.sp
            )
        }
    }
}
