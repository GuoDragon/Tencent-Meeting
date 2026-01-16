package com.appsim.tencent_meeting_sim.presentation.meeting

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.meeting.MembersManageContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.InvitationStatus
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.meeting.MembersManagePresenter
import com.appsim.tencent_meeting_sim.ui.components.TabButton
import com.appsim.tencent_meeting_sim.presentation.meeting.components.SearchAndInviteBar
import com.appsim.tencent_meeting_sim.presentation.meeting.components.MemberItem
import com.appsim.tencent_meeting_sim.presentation.meeting.components.InvitedMemberItem
import com.appsim.tencent_meeting_sim.presentation.meeting.components.InviteContactDialog

@Composable
fun MembersManageScreen(
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
            override fun showMembers(membersList: List<User>) { members = membersList }
            override fun updateMemberMicStatus(userId: String, enabled: Boolean) { }
            override fun updateMemberVideoStatus(userId: String, enabled: Boolean) { }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun showMuteAllSuccess() { errorMessage = context.getString(R.string.msg_all_muted) }
            override fun showUnmuteAllSuccess() { errorMessage = context.getString(R.string.msg_all_unmuted) }
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
                errorMessage = context.getString(R.string.msg_invited_members_count, invitedCount)
                showInviteDialog = false
                inviteLoading = false
                presenter.loadInvitedMembers("meeting001")
            }
            override fun showInviteFailed(message: String) {
                errorMessage = context.getString(R.string.msg_invite_failed, message)
                inviteLoading = false
            }
            override fun updateInvitedMembers(invitedList: List<User>) { invitedMembers = invitedList }
            override fun showRemoveSuccess(userName: String) {
                errorMessage = "已将 $userName 移出会议"
                presenter.loadMembers()
            }
            override fun showRemoveFailed(message: String) {
                errorMessage = message
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
        presenter.loadMembers()
        presenter.loadInvitedMembers("meeting001")
    }

    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.width(48.dp))
                    Text(
                        text = stringResource(R.string.meeting_manage_members),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.icon_desc_close),
                            tint = Color.Black
                        )
                    }
                }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabButton(
                        text = stringResource(R.string.msg_in_meeting_count, members.size),
                        isSelected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        text = stringResource(R.string.msg_not_joined),
                        isSelected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        modifier = Modifier.weight(1f)
                    )
                }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            allMicsMuted = true
                            presenter.muteAll()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                    ) {
                        Text(stringResource(R.string.btn_mute_all), fontSize = 14.sp)
                    }
                    OutlinedButton(
                        onClick = {
                            allMicsMuted = false
                            presenter.unmuteAll()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                    ) {
                        Text(stringResource(R.string.btn_unmute_all), fontSize = 14.sp)
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
                    onSelectAll = {
                        selectedContacts = availableContacts.map { it.userId }.toSet()
                    },
                    onDeselectAll = {
                        selectedContacts = emptySet()
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
private fun MembersList(
    members: List<User>, micEnabled: Boolean, videoEnabled: Boolean,
    allMicsMuted: Boolean, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        items(members) { member ->
            MemberItem(member, micEnabled, videoEnabled, allMicsMuted)
        }
    }
}

@Composable
private fun InvitedMembersList(invitedMembers: List<User>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        if (invitedMembers.isEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.msg_no_invitation_records), fontSize = 14.sp, color = Color.Gray)
                }
            }
        } else {
            items(invitedMembers) { member -> InvitedMemberItem(member = member) }
        }
    }
}
