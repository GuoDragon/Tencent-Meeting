package com.example.tencentmeeting.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import com.example.tencentmeeting.contract.PersonalMeetingRoomContract
import com.example.tencentmeeting.model.PersonalMeetingRoom
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.PersonalMeetingRoomPresenter

@Composable
fun PersonalMeetingRoomPage(
    onNavigateBack: () -> Unit = {},
    onNavigateToMeetingDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val presenter = remember { PersonalMeetingRoomPresenter(context) }

    var roomInfo by remember { mutableStateOf<PersonalMeetingRoom?>(null) }
    var user by remember { mutableStateOf<User?>(null) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showWaitingRoomDialog by remember { mutableStateOf(false) }
    var showAllowBeforeHostDialog by remember { mutableStateOf(false) }
    var showWatermarkDialog by remember { mutableStateOf(false) }
    var showMuteOnEntryDialog by remember { mutableStateOf(false) }
    var showMultiDeviceDialog by remember { mutableStateOf(false) }

    // MVP View implementation
    val view = remember {
        object : PersonalMeetingRoomContract.View {
            override fun showMeetingRoomInfo(newRoomInfo: PersonalMeetingRoom, newUser: User) {
                roomInfo = newRoomInfo
                user = newUser
            }

            override fun updateSettings(newRoomInfo: PersonalMeetingRoom) {
                roomInfo = newRoomInfo
            }

            override fun showError(message: String) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 初始化
    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMeetingRoomInfo("user001") // 当前用户：刘承龙
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD), // 淡蓝色
                        Color(0xFFF5F5F5)  // 灰白色
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 顶部导航栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 48.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 返回按钮
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回",
                        tint = Color(0xFF1976D2)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 右侧操作图标
                IconButton(onClick = { /* 个人资料 */ }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "个人资料",
                        tint = Color(0xFF1976D2)
                    )
                }

                IconButton(onClick = { /* 分享 */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "分享",
                        tint = Color(0xFF1976D2)
                    )
                }
            }

            roomInfo?.let { room ->
                user?.let { currentUser ->
                    // 会议室信息卡片
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            // 头像、名字和操作按钮区域
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 左侧：头像和名字
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
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
                                            text = currentUser.username.first().toString(),
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    // 会议室标题
                                    Text(
                                        text = "${currentUser.username}的个人会议室",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                }

                                // 右侧：编辑资料和二维码按钮
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 编辑资料按钮
                                    OutlinedButton(
                                        onClick = { /* 编辑资料 */ },
                                        modifier = Modifier.width(100.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("编辑资料", fontSize = 12.sp)
                                        }
                                    }

                                    // 二维码按钮
                                    OutlinedButton(
                                        onClick = { /* 二维码 */ },
                                        modifier = Modifier.width(100.dp),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.QrCode,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("二维码", fontSize = 12.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // 会议号
                            MeetingInfoRow(
                                label = "会议号",
                                value = formatMeetingId(room.meetingId),
                                onCopy = {
                                    copyToClipboard(context, room.meetingId, "会议号已复制")
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // 会议链接
                            MeetingInfoRow(
                                label = "会议链接",
                                value = room.meetingLink,
                                onCopy = {
                                    copyToClipboard(context, room.meetingLink, "链接已复制")
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 会议设置卡片
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column {
                            PersonalMeetingRoomSettingItem(
                                title = "入会密码",
                                value = if (room.enablePassword) room.password ?: "" else "未设置",
                                onClick = { showPasswordDialog = true }
                            )
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                            PersonalMeetingRoomSettingItem(
                                title = "等候室",
                                value = if (room.enableWaitingRoom) "开启" else "关闭",
                                onClick = { showWaitingRoomDialog = true }
                            )
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                            PersonalMeetingRoomSettingItem(
                                title = "允许成员在主持人前入会",
                                value = if (room.allowBeforeHost) "是" else "否",
                                onClick = { showAllowBeforeHostDialog = true }
                            )
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                            PersonalMeetingRoomSettingItem(
                                title = "会议水印",
                                value = if (room.enableWatermark) "开启" else "未开启",
                                onClick = { showWatermarkDialog = true }
                            )
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                            PersonalMeetingRoomSettingItem(
                                title = "成员入会时静音",
                                value = room.muteOnEntry,
                                onClick = { showMuteOnEntryDialog = true }
                            )
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                            PersonalMeetingRoomSettingItem(
                                title = "允许成员多端入会",
                                value = if (room.allowMultiDevice) "是" else "否",
                                onClick = { showMultiDeviceDialog = true }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // 进入会议室按钮
                    Button(
                        onClick = { onNavigateToMeetingDetails(room.meetingId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "进入会议室",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        // 密码对话框
        if (showPasswordDialog && roomInfo != null) {
            PasswordDialog(
                currentPassword = roomInfo?.password ?: "",
                currentEnabled = roomInfo?.enablePassword ?: false,
                onDismiss = { showPasswordDialog = false },
                onSave = { password, enabled ->
                    presenter.updatePassword(password, enabled)
                    showPasswordDialog = false
                }
            )
        }

        // 等候室对话框
        if (showWaitingRoomDialog && roomInfo != null) {
            SimpleOptionDialog(
                title = "等候室",
                currentValue = roomInfo?.enableWaitingRoom ?: false,
                options = listOf("关闭" to false, "开启" to true),
                onDismiss = { showWaitingRoomDialog = false },
                onSelect = { enabled ->
                    presenter.updateWaitingRoom(enabled)
                    showWaitingRoomDialog = false
                }
            )
        }

        // 允许成员在主持人前入会对话框
        if (showAllowBeforeHostDialog && roomInfo != null) {
            SimpleOptionDialog(
                title = "允许成员在主持人前入会",
                currentValue = roomInfo?.allowBeforeHost ?: false,
                options = listOf("否" to false, "是" to true),
                onDismiss = { showAllowBeforeHostDialog = false },
                onSelect = { allowed ->
                    presenter.updateAllowBeforeHost(allowed)
                    showAllowBeforeHostDialog = false
                }
            )
        }

        // 会议水印对话框
        if (showWatermarkDialog && roomInfo != null) {
            SimpleOptionDialog(
                title = "会议水印",
                currentValue = roomInfo?.enableWatermark ?: false,
                options = listOf("关闭" to false, "开启" to true),
                onDismiss = { showWatermarkDialog = false },
                onSelect = { enabled ->
                    presenter.updateWatermark(enabled)
                    showWatermarkDialog = false
                }
            )
        }

        // 成员入会时静音对话框
        if (showMuteOnEntryDialog && roomInfo != null) {
            MuteOnEntryDialog(
                currentRule = roomInfo?.muteOnEntry ?: "超过6人后自动开启",
                onDismiss = { showMuteOnEntryDialog = false },
                onSelect = { rule ->
                    presenter.updateMuteOnEntry(rule)
                    showMuteOnEntryDialog = false
                }
            )
        }

        // 允许成员多端入会对话框
        if (showMultiDeviceDialog && roomInfo != null) {
            SimpleOptionDialog(
                title = "允许成员多端入会",
                currentValue = roomInfo?.allowMultiDevice ?: false,
                options = listOf("否" to false, "是" to true),
                onDismiss = { showMultiDeviceDialog = false },
                onSelect = { allowed ->
                    presenter.updateMultiDevice(allowed)
                    showMultiDeviceDialog = false
                }
            )
        }
    }
}

@Composable
fun MeetingInfoRow(
    label: String,
    value: String,
    onCopy: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        IconButton(onClick = onCopy) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "复制",
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun PersonalMeetingRoomSettingItem(
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 15.sp,
            color = Color.Black
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
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
fun PasswordDialog(
    currentPassword: String,
    currentEnabled: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, Boolean) -> Unit
) {
    var password by remember { mutableStateOf(currentPassword) }
    var enabled by remember { mutableStateOf(currentEnabled) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // 顶部栏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消", color = Color(0xFF1976D2))
                    }

                    Text(
                        text = "入会密码",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = { onSave(password, enabled) }) {
                        Text("保存", color = Color(0xFF1976D2))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 启用开关
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("入会密码", fontSize = 16.sp)
                    Switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it }
                    )
                }

                if (enabled) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // 密码输入框
                    OutlinedTextField(
                        value = password,
                        onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) password = it },
                        label = { Text("密码（6位数字）") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleOptionDialog(
    title: String,
    currentValue: Boolean,
    options: List<Pair<String, Boolean>>,
    onDismiss: () -> Unit,
    onSelect: (Boolean) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                options.forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(value) }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(label, fontSize = 16.sp)
                        if (value == currentValue) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "已选",
                                tint = Color(0xFF1976D2)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MuteOnEntryDialog(
    currentRule: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    val options = listOf(
        "关闭",
        "超过6人后自动开启",
        "始终开启"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "成员入会时静音",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(option) }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(option, fontSize = 16.sp)
                        if (option == currentRule) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "已选",
                                tint = Color(0xFF1976D2)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 格式化会议号：4157555988 -> 415 755 5988
fun formatMeetingId(meetingId: String): String {
    return if (meetingId.length == 10) {
        "${meetingId.substring(0, 3)} ${meetingId.substring(3, 6)} ${meetingId.substring(6)}"
    } else {
        meetingId
    }
}

// 复制到剪贴板
fun copyToClipboard(context: Context, text: String, message: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
