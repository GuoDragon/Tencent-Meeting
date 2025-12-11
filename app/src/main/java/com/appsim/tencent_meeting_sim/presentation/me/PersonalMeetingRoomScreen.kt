package com.appsim.tencent_meeting_sim.presentation.me

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.me.PersonalMeetingRoomContract
import com.appsim.tencent_meeting_sim.data.model.PersonalMeetingRoom
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.me.PersonalMeetingRoomPresenter
import com.appsim.tencent_meeting_sim.presentation.me.components.*

@Composable
fun PersonalMeetingRoomScreen(onNavigateBack: () -> Unit = {}, onNavigateToMeetingDetails: (String) -> Unit = {}) {
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

    val view = remember {
        object : PersonalMeetingRoomContract.View {
            override fun showMeetingRoomInfo(newRoomInfo: PersonalMeetingRoom, newUser: User) { roomInfo = newRoomInfo; user = newUser }
            override fun updateSettings(newRoomInfo: PersonalMeetingRoom) { roomInfo = newRoomInfo }
            override fun showError(message: String) { Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadMeetingRoomInfo("user001") }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Box(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5))))) {
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(modifier = Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, top = 48.dp, bottom = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.icon_desc_back), tint = Color(0xFF1976D2))
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { }) { Icon(imageVector = Icons.Default.Person, contentDescription = stringResource(R.string.user_personal_info), tint = Color(0xFF1976D2)) }
                IconButton(onClick = { }) { Icon(imageVector = Icons.Default.Share, contentDescription = stringResource(R.string.icon_desc_share), tint = Color(0xFF1976D2)) }
            }

            roomInfo?.let { room ->
                user?.let { currentUser ->
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFF1976D2)), contentAlignment = Alignment.Center) {
                                        Text(text = currentUser.username.first().toString(), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = stringResource(R.string.label_personal_meeting_room_with_name, currentUser.username), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                                }
                                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    OutlinedButton(onClick = { }, modifier = Modifier.width(100.dp), contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)) {
                                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(stringResource(R.string.btn_edit_profile), fontSize = 12.sp)
                                        }
                                    }
                                    OutlinedButton(onClick = { }, modifier = Modifier.width(100.dp), contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)) {
                                        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                            Icon(imageVector = Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(stringResource(R.string.btn_qr_code), fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            MeetingInfoRow(label = stringResource(R.string.meeting_id_label), value = formatMeetingId(room.meetingId), onCopy = { copyToClipboard(context, room.meetingId, context.getString(R.string.msg_meeting_id_copied)) })
                            Spacer(modifier = Modifier.height(12.dp))
                            MeetingInfoRow(label = stringResource(R.string.label_meeting_link), value = room.meetingLink, onCopy = { copyToClipboard(context, room.meetingLink, context.getString(R.string.msg_link_already_copied)) })
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                        Column {
                            PersonalMeetingRoomSettingItem(title = stringResource(R.string.meeting_password), value = if (room.enablePassword) room.password ?: "" else stringResource(R.string.label_not_set), onClick = { showPasswordDialog = true })
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                            PersonalMeetingRoomSettingItem(title = stringResource(R.string.label_waiting_room), value = if (room.enableWaitingRoom) stringResource(R.string.label_on) else stringResource(R.string.label_off), onClick = { showWaitingRoomDialog = true })
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                            PersonalMeetingRoomSettingItem(title = stringResource(R.string.label_allow_before_host), value = if (room.allowBeforeHost) stringResource(R.string.label_yes) else stringResource(R.string.label_no), onClick = { showAllowBeforeHostDialog = true })
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                            PersonalMeetingRoomSettingItem(title = stringResource(R.string.label_meeting_watermark), value = if (room.enableWatermark) stringResource(R.string.label_on) else stringResource(R.string.label_not_enabled), onClick = { showWatermarkDialog = true })
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                            PersonalMeetingRoomSettingItem(title = stringResource(R.string.label_mute_on_entry), value = room.muteOnEntry, onClick = { showMuteOnEntryDialog = true })
                            Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                            PersonalMeetingRoomSettingItem(title = stringResource(R.string.label_allow_multi_device), value = if (room.allowMultiDevice) stringResource(R.string.label_yes) else stringResource(R.string.label_no), onClick = { showMultiDeviceDialog = true })
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onNavigateToMeetingDetails(room.meetingId) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)), shape = RoundedCornerShape(8.dp)) {
                        Text(text = stringResource(R.string.btn_enter_meeting_room), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (showPasswordDialog && roomInfo != null) {
            PasswordDialog(currentPassword = roomInfo?.password ?: "", currentEnabled = roomInfo?.enablePassword ?: false, onDismiss = { showPasswordDialog = false }, onSave = { password, enabled -> presenter.updatePassword(password, enabled); showPasswordDialog = false })
        }
        if (showWaitingRoomDialog && roomInfo != null) {
            SimpleOptionDialog(title = stringResource(R.string.label_waiting_room), currentValue = roomInfo?.enableWaitingRoom ?: false, options = listOf(stringResource(R.string.label_off) to false, stringResource(R.string.label_on) to true), onDismiss = { showWaitingRoomDialog = false }, onSelect = { presenter.updateWaitingRoom(it); showWaitingRoomDialog = false })
        }
        if (showAllowBeforeHostDialog && roomInfo != null) {
            SimpleOptionDialog(title = stringResource(R.string.label_allow_before_host), currentValue = roomInfo?.allowBeforeHost ?: false, options = listOf(stringResource(R.string.label_no) to false, stringResource(R.string.label_yes) to true), onDismiss = { showAllowBeforeHostDialog = false }, onSelect = { presenter.updateAllowBeforeHost(it); showAllowBeforeHostDialog = false })
        }
        if (showWatermarkDialog && roomInfo != null) {
            SimpleOptionDialog(title = stringResource(R.string.label_meeting_watermark), currentValue = roomInfo?.enableWatermark ?: false, options = listOf(stringResource(R.string.label_off) to false, stringResource(R.string.label_on) to true), onDismiss = { showWatermarkDialog = false }, onSelect = { presenter.updateWatermark(it); showWatermarkDialog = false })
        }
        if (showMuteOnEntryDialog && roomInfo != null) {
            MuteOnEntryDialog(currentRule = roomInfo?.muteOnEntry ?: "超过6人后自动开启", onDismiss = { showMuteOnEntryDialog = false }, onSelect = { presenter.updateMuteOnEntry(it); showMuteOnEntryDialog = false })
        }
        if (showMultiDeviceDialog && roomInfo != null) {
            SimpleOptionDialog(title = stringResource(R.string.label_allow_multi_device), currentValue = roomInfo?.allowMultiDevice ?: false, options = listOf(stringResource(R.string.label_no) to false, stringResource(R.string.label_yes) to true), onDismiss = { showMultiDeviceDialog = false }, onSelect = { presenter.updateMultiDevice(it); showMultiDeviceDialog = false })
        }
    }
}

fun formatMeetingId(meetingId: String): String = if (meetingId.length == 10) "${meetingId.substring(0, 3)} ${meetingId.substring(3, 6)} ${meetingId.substring(6)}" else meetingId

fun copyToClipboard(context: Context, text: String, message: String) {
    (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("label", text))
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
