package com.appsim.tencent_meeting_sim.presentation.meeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.presentation.meeting.components.*

@Composable
fun HistoryMeetingsScreen(onNavigateBack: () -> Unit = {}, onNavigateToMeetingReplay: (String) -> Unit = {}) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { HistoryMeetingsPresenter(dataRepository) }
    var historyMeetings by remember { mutableStateOf<List<Meeting>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showEmptyState by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val view = remember {
        object : HistoryMeetingsContract.View {
            override fun showMeetings(meetings: List<Meeting>) { historyMeetings = meetings; showEmptyState = false }
            override fun showEmptyState() { historyMeetings = emptyList(); showEmptyState = true }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun navigateToMeetingReplay(meetingId: String) { onNavigateToMeetingReplay(meetingId) }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadHistoryMeetings() }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    val filteredMeetings = remember(historyMeetings, searchQuery) { if (searchQuery.isBlank()) historyMeetings else historyMeetings.filter { it.topic.contains(searchQuery, ignoreCase = true) || it.meetingId.contains(searchQuery, ignoreCase = true) || it.hostId.contains(searchQuery, ignoreCase = true) } }

    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5)))).padding(top = 24.dp)) {
        HistoryTopBar(onNavigateBack = onNavigateBack)
        HistorySearchBar(searchQuery = searchQuery, onSearchQueryChange = { searchQuery = it })
        when {
            isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color(0xFF1976D2)) }
            showEmptyState -> EmptyHistoryState()
            else -> LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { items(filteredMeetings) { meeting -> HistoryMeetingItem(meeting = meeting, onClick = { presenter.onMeetingClicked(meeting.meetingId) }) } }
        }
        errorMessage?.let { message -> LaunchedEffect(message) { errorMessage = null } }
    }
}
