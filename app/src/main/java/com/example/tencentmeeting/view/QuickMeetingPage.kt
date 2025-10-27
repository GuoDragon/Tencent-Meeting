package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.QuickMeetingContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.QuickMeetingPresenter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickMeetingPage(
    onNavigateBack: () -> Unit,
    onNavigateToMeetingDetails: (String, Boolean, Boolean, Boolean, Boolean) -> Unit = { _, _, _, _, _ -> }
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { QuickMeetingPresenter(dataRepository) }

    var currentUser by remember { mutableStateOf<User?>(null) }
    var videoEnabled by remember { mutableStateOf(false) }
    var micEnabled by remember { mutableStateOf(true) }
    var speakerEnabled by remember { mutableStateOf(true) }
    var recordingEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessMessage by remember { mutableStateOf(false) }

    // MVP View实现
    val view = remember {
        object : QuickMeetingContract.View {
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

            override fun showStartMeetingSuccess(meetingId: String, micEnabled: Boolean, videoEnabled: Boolean, speakerEnabled: Boolean) {
                onNavigateToMeetingDetails(meetingId, micEnabled, videoEnabled, speakerEnabled, recordingEnabled)
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
                        text = "快速会议",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "退出",
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
                        // 入会姓名
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "入会姓名",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "刘承龙",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        }

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 麦克风是否开启
                        SwitchSettingItem(
                            title = "麦克风是否开启",
                            checked = micEnabled,
                            onCheckedChange = { micEnabled = it }
                        )

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 摄像头是否开启
                        SwitchSettingItem(
                            title = "摄像头是否开启",
                            checked = videoEnabled,
                            onCheckedChange = { videoEnabled = it }
                        )

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 扬声器是否开启
                        SwitchSettingItem(
                            title = "扬声器是否开启",
                            checked = speakerEnabled,
                            onCheckedChange = { speakerEnabled = it }
                        )

                        Divider(
                            color = Color(0xFFEEEEEE),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // 会议录制
                        SwitchSettingItem(
                            title = "会议录制",
                            checked = recordingEnabled,
                            onCheckedChange = { recordingEnabled = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // 底部开始会议按钮
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        presenter.startQuickMeeting(
                            videoEnabled = videoEnabled,
                            usePersonalMeetingId = false,
                            micEnabled = micEnabled,
                            speakerEnabled = speakerEnabled
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1976D2)
                    )
                ) {
                    Text(
                        text = "开始会议",
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 开始会议成功提示
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
                            text = "正在启动会议...",
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
