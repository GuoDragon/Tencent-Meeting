package com.appsim.tencent_meeting_sim.presentation.meeting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Message
import com.appsim.tencent_meeting_sim.presentation.meeting.components.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingChatScreen(meetingId: String, onClose: () -> Unit) {
    val context = LocalContext.current
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MeetingChatPresenter(dataRepository) }

    val view = remember {
        object : MeetingChatContract.View {
            override fun showMessages(newMessages: List<Message>) { messages = newMessages }
            override fun addNewMessage(message: Message) { messages = messages + message }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun clearInput() { inputText = "" }
            override fun scrollToLatest() { coroutineScope.launch { if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1) } }
        }
    }

    LaunchedEffect(meetingId) { presenter.attachView(view); presenter.loadMessages(meetingId) }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Scaffold(topBar = { MeetingChatTopBar(onClose = onClose) }, bottomBar = { MeetingChatInputBar(inputText = inputText, onInputChange = { inputText = it }, onSend = { presenter.sendMessage(meetingId, inputText) }) }) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues).background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5))))) {
            when {
                isLoading && messages.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                messages.isEmpty() -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(text = stringResource(R.string.msg_empty_messages), color = Color.Gray, fontSize = 16.sp) }
                else -> LazyColumn(state = listState, modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { items(messages) { message -> MessageItem(message = message) } }
            }
            errorMessage?.let { error -> Snackbar(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp), action = { TextButton(onClick = { errorMessage = null }) { Text(stringResource(R.string.icon_desc_close)) } }) { Text(error) } }
        }
    }
}
