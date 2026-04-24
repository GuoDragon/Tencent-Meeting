package com.appsim.tencent_meeting_sim.presentation.meeting

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.meeting.components.SwitchSettingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickMeetingScreen(onNavigateBack: () -> Unit, onNavigateToMeetingDetails: (String, Boolean, Boolean, Boolean, Boolean) -> Unit = { _, _, _, _, _ -> }) {
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

    val view = remember {
        object : QuickMeetingContract.View {
            override fun showUserInfo(user: User) { currentUser = user }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun showStartMeetingSuccess(meetingId: String, micEnabled: Boolean, videoEnabled: Boolean, speakerEnabled: Boolean) { onNavigateToMeetingDetails(meetingId, micEnabled, videoEnabled, speakerEnabled, recordingEnabled) }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadUserInfo() }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.meeting_quick), fontSize = 18.sp, fontWeight = FontWeight.Medium) }, navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.icon_desc_close), tint = Color.Black) } }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE3F2FD))) }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5))))) {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.user_entry_name), fontSize = 16.sp, color = Color.Black)
                            Text(text = "刘承龙", fontSize = 16.sp, color = Color.Black)
                        }
                        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                        SwitchSettingItem(title = stringResource(R.string.device_mic_enabled_question), checked = micEnabled, onCheckedChange = { micEnabled = it })
                        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                        SwitchSettingItem(title = stringResource(R.string.device_camera_enabled_question), checked = videoEnabled, onCheckedChange = { videoEnabled = it })
                        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                        SwitchSettingItem(title = stringResource(R.string.device_speaker_enabled_question), checked = speakerEnabled, onCheckedChange = { speakerEnabled = it })
                        Divider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                        SwitchSettingItem(title = stringResource(R.string.device_recording), checked = recordingEnabled, onCheckedChange = { recordingEnabled = it })
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
            Column(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp)) {
                Button(onClick = { presenter.startQuickMeeting(videoEnabled = videoEnabled, usePersonalMeetingId = false, micEnabled = micEnabled, speakerEnabled = speakerEnabled) }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))) {
                    Text(text = stringResource(R.string.meeting_start), fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Medium)
                }
            }
            if (showSuccessMessage) {
                LaunchedEffect(Unit) { kotlinx.coroutines.delay(2000); showSuccessMessage = false; onNavigateBack() }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))) { Text(text = stringResource(R.string.msg_starting_meeting), color = Color.White, modifier = Modifier.padding(16.dp)) } }
            }
            errorMessage?.let { message -> LaunchedEffect(message) { kotlinx.coroutines.delay(2000); errorMessage = null } }
        }
    }
}
