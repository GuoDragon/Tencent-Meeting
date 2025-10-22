package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.JoinMeetingContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.JoinMeetingPresenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinMeetingPage(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { JoinMeetingPresenter(dataRepository) }

    var currentUser by remember { mutableStateOf<User?>(null) }
    var meetingId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var useNameAlways by remember { mutableStateOf(true) }
    var micEnabled by remember { mutableStateOf(false) }
    var speakerEnabled by remember { mutableStateOf(true) }
    var videoEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // MVP View实现
    val view = remember {
        object : JoinMeetingContract.View {
            override fun showUserInfo(user: User) {
                currentUser = user
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

            override fun showJoinSuccess(meetingId: String) {
                showSuccessMessage = true
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "加入会议",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "返回",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // 中间功能区卡片
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // 会议号输入框
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "会议号",
                                fontSize = 16.sp,
                                color = Color.Black,
                                modifier = Modifier.width(80.dp)
                            )
                            OutlinedTextField(
                                value = meetingId,
                                onValueChange = { meetingId = it },
                                placeholder = {
                                    Text(
                                        text = "请输入会议号",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )
                                },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Color(0xFF1976D2)
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 您的姓名
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "您的姓名",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = currentUser?.username ?: "刘承龙",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 当前设备始终使用此名称入会
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "当前设备始终使用此名称入会",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Checkbox(
                                checked = useNameAlways,
                                onCheckedChange = { useNameAlways = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF1976D2)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 设备设置卡片
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // 开启麦克风
                        SwitchSettingItem(
                            title = "开启麦克风",
                            checked = micEnabled,
                            onCheckedChange = { micEnabled = it }
                        )

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 开启扬声器
                        SwitchSettingItem(
                            title = "开启扬声器",
                            checked = speakerEnabled,
                            onCheckedChange = { speakerEnabled = it }
                        )

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 开启视频
                        SwitchSettingItem(
                            title = "开启视频",
                            checked = videoEnabled,
                            onCheckedChange = { videoEnabled = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // 底部加入会议按钮
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (meetingId.isNotEmpty()) {
                            presenter.joinMeeting(
                                meetingId = meetingId,
                                password = password.ifEmpty { null },
                                micEnabled = micEnabled,
                                speakerEnabled = speakerEnabled,
                                videoEnabled = videoEnabled
                            )
                        }
                    },
                    enabled = meetingId.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (meetingId.isNotEmpty()) Color(0xFF1976D2) else Color(0xFFB0BEC5),
                        disabledContainerColor = Color(0xFFB0BEC5)
                    )
                ) {
                    Text(
                        text = "加入会议",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 加入成功提示
            if (showSuccessMessage) {
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    showSuccessMessage = false
                    onNavigateBack()
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text(
                            text = "正在加入会议...",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
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
private fun SwitchSettingItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF1976D2)
            )
        )
    }
}
