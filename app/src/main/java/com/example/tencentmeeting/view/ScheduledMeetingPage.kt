package com.example.tencentmeeting.view

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.ScheduledMeetingContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.RecurrenceType
import com.example.tencentmeeting.model.User
import com.example.tencentmeeting.presenter.ScheduledMeetingPresenter
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduledMeetingPage(
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

            override fun showError(message: String) {
                // 可以使用Snackbar显示错误
            }

            override fun showSuccess(message: String) {
                // Show success message, navigateBack will be called by presenter
            }

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
                        text = "预定会议",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(
                            text = "取消",
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
                            text = "完成",
                            color = Color(0xFF1976D2),
                            fontSize = 16.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
        ) {
            // 会议主题
            Spacer(modifier = Modifier.height(16.dp))
            MeetingTopicSection(meetingTopic)

            Spacer(modifier = Modifier.height(16.dp))

            // 开始时间
            SettingItem(
                title = "开始时间",
                value = formatDateTime(startTime),
                onClick = { showDatePicker = true }
            )

            // 会议时长
            SettingItem(
                title = "会议时长",
                value = "${duration}分钟",
                onClick = { showDurationPicker = true }
            )

            // 重复频率
            SettingItem(
                title = "重复频率",
                value = recurrence.displayName,
                onClick = { showRecurrencePicker = true }
            )

            // 参会人
            SettingItem(
                title = "参会人",
                value = if (selectedParticipants.isEmpty()) "添加" else "${selectedParticipants.size}人",
                onClick = { showParticipantPicker = true }
            )

            // 入会密码
            SwitchSettingItem(
                title = "入会密码",
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
                title = "会议录制",
                checked = recordingEnabled,
                onCheckedChange = { recordingEnabled = it }
            )
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
                availableUsers = availableUsers,
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
}

@Composable
private fun MeetingTopicSection(topic: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = topic,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
    Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
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
            .background(Color.White)
            .padding(16.dp),
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

@Composable
private fun DurationPickerDialog(
    currentDuration: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val durations = listOf(15, 30, 45, 60, 90, 120)
    var selectedDuration by remember { mutableStateOf(currentDuration) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择会议时长") },
        text = {
            Column {
                durations.forEach { duration ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDuration = duration }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDuration == duration,
                            onClick = { selectedDuration = duration }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${duration}分钟")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedDuration) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun RecurrencePickerDialog(
    currentRecurrence: RecurrenceType,
    onConfirm: (RecurrenceType) -> Unit,
    onDismiss: () -> Unit
) {
    val recurrenceTypes = RecurrenceType.values()
    var selectedRecurrence by remember { mutableStateOf(currentRecurrence) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择重复频率") },
        text = {
            Column {
                recurrenceTypes.forEach { recurrence ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedRecurrence = recurrence }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRecurrence == recurrence,
                            onClick = { selectedRecurrence = recurrence }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(recurrence.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedRecurrence) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun ParticipantPickerDialog(
    availableUsers: List<User>,
    selectedUsers: List<User>,
    onConfirm: (List<User>) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedList by remember { mutableStateOf(selectedUsers) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择参会人") },
        text = {
            Column(
                modifier = Modifier.height(300.dp)
            ) {
                availableUsers.forEach { user ->
                    val isSelected = selectedList.contains(user)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedList = if (isSelected) {
                                    selectedList - user
                                } else {
                                    selectedList + user
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                selectedList = if (checked) {
                                    selectedList + user
                                } else {
                                    selectedList - user
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(user.username, fontWeight = FontWeight.Medium)
                            user.phone?.let { phone ->
                                Text(phone, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedList) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun PasswordInputDialog(
    currentPassword: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var password by remember { mutableStateOf(currentPassword) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("设置入会密码") },
        text = {
            Column {
                Text("请输入6位数字密码", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                            password = it
                        }
                    },
                    placeholder = { Text("6位数字") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(password) },
                enabled = password.length == 6
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun DatePickerDialog(
    currentTime: Long,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择日期") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 年份选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Icon(Icons.Default.Remove, "减少年份")
                    }
                    Text("${selectedYear}年", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { selectedYear++ }) {
                        Icon(Icons.Default.Add, "增加年份")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 月份选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (selectedMonth > 0) selectedMonth--
                        else { selectedMonth = 11; selectedYear-- }
                    }) {
                        Icon(Icons.Default.Remove, "减少月份")
                    }
                    Text("${selectedMonth + 1}月", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        if (selectedMonth < 11) selectedMonth++
                        else { selectedMonth = 0; selectedYear++ }
                    }) {
                        Icon(Icons.Default.Add, "增加月份")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 日期选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val maxDays = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth)
                        set(Calendar.DAY_OF_MONTH, 1)
                    }.getActualMaximum(Calendar.DAY_OF_MONTH)

                    IconButton(onClick = {
                        if (selectedDay > 1) selectedDay--
                    }) {
                        Icon(Icons.Default.Remove, "减少日期")
                    }
                    Text("${selectedDay}日", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        if (selectedDay < maxDays) selectedDay++
                    }) {
                        Icon(Icons.Default.Add, "增加日期")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newCalendar = Calendar.getInstance().apply {
                    timeInMillis = currentTime
                    set(Calendar.YEAR, selectedYear)
                    set(Calendar.MONTH, selectedMonth)
                    set(Calendar.DAY_OF_MONTH, selectedDay)
                }
                onConfirm(newCalendar.timeInMillis)
            }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
private fun TimePickerDialog(
    currentTime: Long,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }
    var selectedHour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择时间") },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 小时选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (selectedHour > 0) selectedHour-- else selectedHour = 23
                    }) {
                        Icon(Icons.Default.Remove, "减少小时")
                    }
                    Text(String.format("%02d", selectedHour), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        if (selectedHour < 23) selectedHour++ else selectedHour = 0
                    }) {
                        Icon(Icons.Default.Add, "增加小时")
                    }
                }

                Text(":", fontSize = 32.sp, fontWeight = FontWeight.Bold)

                // 分钟选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (selectedMinute > 0) selectedMinute-- else selectedMinute = 59
                    }) {
                        Icon(Icons.Default.Remove, "减少分钟")
                    }
                    Text(String.format("%02d", selectedMinute), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        if (selectedMinute < 59) selectedMinute++ else selectedMinute = 0
                    }) {
                        Icon(Icons.Default.Add, "增加分钟")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newCalendar = Calendar.getInstance().apply {
                    timeInMillis = currentTime
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                }
                onConfirm(newCalendar.timeInMillis)
            }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

private fun formatDateTime(timeMillis: Long): String {
    val sdf = SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA)
    return sdf.format(Date(timeMillis))
}
