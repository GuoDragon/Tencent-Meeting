package com.appsim.tencent_meeting_sim.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.appsim.tencent_meeting_sim.presentation.home.HomeContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.home.HomePresenter
import com.appsim.tencent_meeting_sim.presentation.home.components.UserInfoCard
import com.appsim.tencent_meeting_sim.presentation.home.components.HomeFunctionButton
import com.appsim.tencent_meeting_sim.presentation.home.components.MeetingItem

@Composable
fun HomeScreen(
    onNavigateToScheduledMeeting: () -> Unit = {}, onNavigateToJoinMeeting: () -> Unit = {}, onNavigateToQuickMeeting: () -> Unit = {},
    onNavigateToMeetingDetails: (String) -> Unit = {}, onNavigateToScheduledMeetingDetails: (String) -> Unit = {},
    onNavigateToHistoryMeetings: () -> Unit = {}, onNavigateToShareScreen: () -> Unit = {}, onNavigateToMeTab: () -> Unit = {}
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { HomePresenter(dataRepository) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var currentMeetings by remember { mutableStateOf<List<Meeting>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showEmptyState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : HomeContract.View {
            override fun showMeetings(meetings: List<Meeting>) { currentMeetings = meetings; showEmptyState = false }
            override fun showEmptyMeetings() { currentMeetings = emptyList(); showEmptyState = true }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun navigateToJoinMeeting() { onNavigateToJoinMeeting() }
            override fun navigateToQuickMeeting() { onNavigateToQuickMeeting() }
            override fun navigateToScheduledMeeting() { onNavigateToScheduledMeeting() }
            override fun showUserInfo(user: User) { currentUser = user }
            override fun showHistoryMeetings(meetings: List<Meeting>) { }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadCurrentUser(); presenter.loadMeetings() }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5))))) {
        Spacer(modifier = Modifier.height(24.dp))

        currentUser?.let { user -> UserInfoCard(user = user, onUserInfoClick = onNavigateToMeTab) }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
            HomeFunctionButton(icon = Icons.Default.Add, text = stringResource(R.string.meeting_join), onClick = { presenter.onJoinMeetingClicked() })
            HomeFunctionButton(icon = Icons.Default.Bolt, text = stringResource(R.string.meeting_quick), onClick = { presenter.onQuickMeetingClicked() })
            HomeFunctionButton(icon = Icons.Default.Schedule, text = stringResource(R.string.meeting_scheduled), onClick = { presenter.onScheduledMeetingClicked() })
            HomeFunctionButton(icon = Icons.Default.ScreenShare, text = stringResource(R.string.meeting_share_screen), onClick = onNavigateToShareScreen)
        }

        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(R.string.meeting_list), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Row(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(Color(0xFFF0F0F0)).clickable { onNavigateToHistoryMeetings() }.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.meeting_history), fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ChevronRight, contentDescription = stringResource(R.string.action_view_history), tint = Color.Gray, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF1976D2))
                    }
                }
                showEmptyState -> {
                    Column(modifier = Modifier.fillMaxWidth().height(200.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Icon(imageVector = Icons.Default.CloudOff, contentDescription = stringResource(R.string.msg_empty_meetings), tint = Color.Gray, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = stringResource(R.string.msg_empty_meetings), fontSize = 16.sp, color = Color.Gray)
                    }
                }
                else -> {
                    LazyColumn {
                        items(currentMeetings) { meeting ->
                            MeetingItem(meeting = meeting, onNavigateToMeetingDetails = onNavigateToMeetingDetails, onNavigateToScheduledMeetingDetails = onNavigateToScheduledMeetingDetails)
                        }
                    }
                }
            }
        }

        errorMessage?.let { message -> LaunchedEffect(message) { errorMessage = null } }
    }
}
