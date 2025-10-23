package com.example.tencentmeeting.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.contract.MeetingChatContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Message
import com.example.tencentmeeting.presenter.MeetingChatPresenter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * 会议聊天页面
 * 显示会议中的聊天消息，支持发送文本消息
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingChatPage(
    meetingId: String,
    onClose: () -> Unit
) {
    val context = LocalContext.current

    // 状态变量
    var messages by remember { mutableStateOf<List<Message>>(emptyList()) }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // LazyColumn滚动状态
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 创建Presenter
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MeetingChatPresenter(dataRepository) }

    // 创建View接口实现
    val view = remember {
        object : MeetingChatContract.View {
            override fun showMessages(newMessages: List<Message>) {
                messages = newMessages
            }

            override fun addNewMessage(message: Message) {
                messages = messages + message
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

            override fun clearInput() {
                inputText = ""
            }

            override fun scrollToLatest() {
                coroutineScope.launch {
                    if (messages.isNotEmpty()) {
                        listState.animateScrollToItem(messages.size - 1)
                    }
                }
            }
        }
    }

    // 初始化
    LaunchedEffect(meetingId) {
        presenter.attachView(view)
        presenter.loadMessages(meetingId)
    }

    // 清理
    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    // 主UI
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "聊天",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            // 底部输入区域
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 输入框
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        placeholder = { Text("请输入消息...") },
                        maxLines = 3,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF1976D2),
                            unfocusedBorderColor = Color.LightGray
                        )
                    )

                    // 发送按钮
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                presenter.sendMessage(meetingId, inputText)
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = if (inputText.isNotBlank()) Color(0xFF1976D2) else Color.LightGray,
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "发送",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // 消息列表
            if (isLoading && messages.isEmpty()) {
                // 加载中
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (messages.isEmpty()) {
                // 空状态
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无消息",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            } else {
                // 消息列表
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { message ->
                        MessageItem(message = message)
                    }
                }
            }

            // 错误提示
            errorMessage?.let { error ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { errorMessage = null }) {
                            Text("关闭")
                        }
                    }
                ) {
                    Text(error)
                }
            }
        }
    }
}

/**
 * 单条消息UI组件
 */
@Composable
fun MessageItem(message: Message) {
    val isMyMessage = message.senderName == "我"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMyMessage) Alignment.End else Alignment.Start
    ) {
        // 发送者和时间
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (isMyMessage) Arrangement.End else Arrangement.Start
        ) {
            Text(
                text = message.senderName,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatTimestamp(message.timestamp),
                fontSize = 12.sp,
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 消息气泡
        Surface(
            shape = RoundedCornerShape(
                topStart = if (isMyMessage) 12.dp else 4.dp,
                topEnd = if (isMyMessage) 4.dp else 12.dp,
                bottomStart = 12.dp,
                bottomEnd = 12.dp
            ),
            color = if (isMyMessage) Color(0xFF1976D2) else Color.White,
            shadowElevation = 2.dp
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                color = if (isMyMessage) Color.White else Color.Black,
                fontSize = 15.sp
            )
        }
    }
}

/**
 * 格式化时间戳
 */
private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
