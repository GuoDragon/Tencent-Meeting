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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tencentmeeting.contract.MembersManageContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.InvitationStatus
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

    // 邀请对话框相关状态
    var showInviteDialog by remember { mutableStateOf(false) }
    var availableContacts by remember { mutableStateOf<List<User>>(emptyList()) }
    var selectedContacts by remember { mutableStateOf<Set<String>>(emptySet()) }
    var inviteLoading by remember { mutableStateOf(false) }

    // 已邀请但未入��的成员列表
    var invitedMembers by remember { mutableStateOf<List<User>>(emptyList()) }

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

            // 新增邀请相关方法实现
            override fun showInviteDialog(availableContactsList: List<User>) {
                availableContacts = availableContactsList
                selectedContacts = emptySet()
                showInviteDialog = true
            }

            override fun hideInviteDialog() {
                showInviteDialog = false
                selectedContacts = emptySet()
            }

            override fun showInviteSuccess(invitedCount: Int) {
                errorMessage = "已成功邀请 $invitedCount 位成员"
                showInviteDialog = false
                inviteLoading = false

                // 重新加载已邀请成员列表
                try {
                    val invitations = dataRepository.getInvitationsByMeetingId("meeting001")
                        .filter { it.status == InvitationStatus.PENDING }

                    val allUsers = dataRepository.getUsers()
                    val invitedUsers = allUsers.filter { user ->
                        invitations.any { it.inviteeId == user.userId }
                    }
                    updateInvitedMembers(invitedUsers)
                } catch (e: Exception) {
                    // 忽略错误，不影响主要功能
                }
            }

            override fun showInviteFailed(message: String) {
                errorMessage = "邀请失败: $message"
                inviteLoading = false
            }

            fun updateInvitedMembers(invitedList: List<User>) {
                invitedMembers = invitedList
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMembers()

        // 加载已邀请但未入会的成员
        try {
            val invitations = dataRepository.getInvitationsByMeetingId("meeting001")
                .filter { it.status == InvitationStatus.PENDING }

            val allUsers = dataRepository.getUsers()
            val invitedUsers = allUsers.filter { user ->
                invitations.any { it.inviteeId == user.userId }
            }
            view.updateInvitedMembers(invitedUsers)
        } catch (e: Exception) {
            // 忽略错误，不影响主要功能
        }
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
                    InvitedMembersList(
                        invitedMembers = invitedMembers,
                        modifier = Modifier.weight(1f)
                    )
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

            // 邀请对话框
            if (showInviteDialog) {
                InviteContactDialog(
                    contacts = availableContacts,
                    selectedContacts = selectedContacts,
                    onContactToggle = { contactId ->
                        selectedContacts = if (selectedContacts.contains(contactId)) {
                            selectedContacts - contactId
                        } else {
                            selectedContacts + contactId
                        }
                    },
                    onInvite = {
                        if (selectedContacts.isNotEmpty()) {
                            inviteLoading = true
                            presenter.sendInvitations(selectedContacts.toList())
                        }
                    },
                    onDismiss = {
                        showInviteDialog = false
                    },
                    isLoading = inviteLoading
                )
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
private fun InvitedMembersList(
    invitedMembers: List<User>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        if (invitedMembers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无邀请记录",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(invitedMembers) { member ->
                InvitedMemberItem(member = member)
            }
        }
    }
}

@Composable
private fun InvitedMemberItem(member: User) {
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
                text = "已邀请",
                fontSize = 12.sp,
                color = Color(0xFF4CAF50)
            )
        }

        // 邀请状态
        Text(
            text = "待响应",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .background(
                    Color(0xFFF5F5F5),
                    RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
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

@Composable
private fun InviteContactDialog(
    contacts: List<User>,
    selectedContacts: Set<String>,
    onContactToggle: (String) -> Unit,
    onInvite: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false
) {
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
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                // 顶部标题栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "邀请新成员",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭",
                            tint = Color.Gray
                        )
                    }
                }

                // 联系人列表
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(contacts) { contact ->
                        ContactSelectionItem(
                            contact = contact,
                            isSelected = selectedContacts.contains(contact.userId),
                            onToggle = { onContactToggle(contact.userId) }
                        )
                    }
                }

                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 取消按钮
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = "取消",
                            fontSize = 16.sp
                        )
                    }

                    // 邀请按钮
                    Button(
                        onClick = onInvite,
                        enabled = selectedContacts.isNotEmpty() && !isLoading,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = "邀请 (${selectedContacts.size})",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactSelectionItem(
    contact: User,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 选择框
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 头像
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.username.firstOrNull()?.toString() ?: "U",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 联系人信息
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                contact.phone?.let { phone ->
                    Text(
                        text = phone,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
