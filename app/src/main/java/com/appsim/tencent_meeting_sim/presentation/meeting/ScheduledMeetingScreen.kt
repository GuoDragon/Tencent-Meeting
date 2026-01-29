package com.appsim.tencent_meeting_sim.presentation.meeting

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.RecurrenceType
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingPresenter
import com.appsim.tencent_meeting_sim.ui.components.SettingItem
import com.appsim.tencent_meeting_sim.ui.components.SwitchSettingItem
import com.appsim.tencent_meeting_sim.presentation.meeting.components.DatePickerDialog
import com.appsim.tencent_meeting_sim.presentation.meeting.components.TimePickerDialog
import com.appsim.tencent_meeting_sim.presentation.meeting.components.DurationPickerDialog
import com.appsim.tencent_meeting_sim.presentation.meeting.components.RecurrencePickerDialog
import com.appsim.tencent_meeting_sim.presentation.meeting.components.ParticipantPickerDialog
import com.appsim.tencent_meeting_sim.presentation.meeting.components.PasswordInputDialog
import com.appsim.tencent_meeting_sim.common.utils.DateTimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledMeetingScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMeetingDetails: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { ScheduledMeetingPresenter(dataRepository) }

    // UI状态
    var meetingTopic by remember { mutableStateOf("刘承龙预定的会议") }
    var startTime by remember { mutableStateOf(System.currentTimeMillis() + 3600000) } // 默认1小时后
    var duration by remember { mutableStateOf(30) } // 默认30分钟
    var recurrence by remember { mutableStateOf(RecurrenceType.NONE) }
    var selectedParticipants by remember { mutableStateOf<List<User>>(emptyList()) }
    var passwordEnabled by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var recordingEnabled by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showDurationPicker by remember { mutableStateOf(false) }
    var showRecurrencePicker by remember { mutableStateOf(false) }
    var showParticipantPicker by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var availableUsers by remember { mutableStateOf<List<User>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // MVP View实现
    val view = remember {
        object : ScheduledMeetingContract.View {
            override fun showUsers(users: List<User>) {
                availableUsers = users
            }
            override fun showLoading() {
                isLoading = true
            }
            override fun hideLoading() {
                isLoading = false
            }
            override fun showError(message: String) { }
            override fun showSuccess(message: String) { }
            override fun navigateBack() {
                onNavigateBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadUsers()
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
                        text = stringResource(R.string.meeting_scheduled),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(
                            text = stringResource(R.string.btn_cancel),
                            color = Color(0xFF1976D2),
                            fontSize = 16.sp
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            presenter.saveMeeting(
                                topic = meetingTopic,
                                startTime = startTime,
                                duration = duration,
                                recurrence = recurrence.name,
                                participantIds = selectedParticipants.map { it.userId },
                                password = if (passwordEnabled) password else null
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.btn_done),
                            color = Color(0xFF1976D2),
                            fontSize = 16.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE3F2FD)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
            // 会议主题 - 可编辑的TextField
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = meetingTopic,
                    onValueChange = { meetingTopic = it },
                    placeholder = {
                        Text(
                            text = "请输入会议主题",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = Color(0xFF1976D2)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 开始时间
            SettingItem(
                title = stringResource(R.string.label_start_time),
                value = DateTimeUtils.formatDateTime(startTime),
                onClick = { showDatePicker = true }
            )

            // 会议时长
            SettingItem(
                title = stringResource(R.string.meeting_duration),
                value = stringResource(R.string.label_minutes_with_value, duration),
                onClick = { showDurationPicker = true }
            )

            // 重复频率
            SettingItem(
                title = stringResource(R.string.meeting_recurrence_freq),
                value = recurrence.displayName,
                onClick = { showRecurrencePicker = true }
            )

            // 参会人
            SettingItem(
                title = stringResource(R.string.meeting_participants),
                value = if (selectedParticipants.isEmpty()) stringResource(R.string.btn_add) else stringResource(R.string.label_people_count, selectedParticipants.size),
                onClick = { showParticipantPicker = true }
            )

            // 入会密码
            SwitchSettingItem(
                title = stringResource(R.string.meeting_password),
                checked = passwordEnabled,
                onCheckedChange = { enabled ->
                    passwordEnabled = enabled
                    if (enabled) {
                        showPasswordDialog = true
                    } else {
                        password = ""
                    }
                }
            )

            // 会议录制
            SwitchSettingItem(
                title = stringResource(R.string.device_recording),
                checked = recordingEnabled,
                onCheckedChange = { recordingEnabled = it }
            )
        }
    }

    // 日期选择器
    if (showDatePicker) {
        DatePickerDialog(
            currentTime = startTime,
            onConfirm = { newTime ->
                startTime = newTime
                showDatePicker = false
                showTimePicker = true
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // 时间选择器
    if (showTimePicker) {
        TimePickerDialog(
            currentTime = startTime,
            onConfirm = { newTime ->
                startTime = newTime
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    // 会议时长选择器
    if (showDurationPicker) {
        DurationPickerDialog(
            currentDuration = duration,
            onConfirm = { newDuration ->
                duration = newDuration
                showDurationPicker = false
            },
            onDismiss = { showDurationPicker = false }
        )
    }

    // 重复频率选择器
    if (showRecurrencePicker) {
        RecurrencePickerDialog(
            currentRecurrence = recurrence,
            onConfirm = { newRecurrence ->
                recurrence = newRecurrence
                showRecurrencePicker = false
            },
            onDismiss = { showRecurrencePicker = false }
        )
    }

    // 参会人选择器
    if (showParticipantPicker) {
        ParticipantPickerDialog(
            availableUsers = availableUsers.filter { it.userId != "user001" }, // 过滤掉当前用户"刘承龙"
            selectedUsers = selectedParticipants,
            onConfirm = { selected ->
                selectedParticipants = selected
                showParticipantPicker = false
            },
            onDismiss = { showParticipantPicker = false }
        )
    }

    // 密码输入对话框
    if (showPasswordDialog) {
        PasswordInputDialog(
            currentPassword = password,
            onConfirm = { newPassword ->
                password = newPassword
                showPasswordDialog = false
            },
            onDismiss = {
                passwordEnabled = false
                showPasswordDialog = false
            }
        )
    }
}
