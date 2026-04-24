package com.appsim.tencent_meeting_sim.presentation.meeting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.presentation.meeting.ShareScreenInputContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository

@Composable
fun ShareScreenInputScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToMeetingDetails: (String, Boolean) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { ShareScreenInputPresenter(dataRepository) }

    var meetingId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : ShareScreenInputContract.View {
            override fun showError(message: String) {
                errorMessage = message
            }

            override fun navigateToMeetingDetails(meetingId: String) {
                // 导航到会议详情页，并启用屏幕共享
                onNavigateToMeetingDetails(meetingId, true)
            }
        }
    }

    LaunchedEffect(Unit) {
        presenter.attachView(view)
    }

    DisposableEffect(Unit) {
        onDispose {
            presenter.onDestroy()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFF5F5F5)
                    )
                )
            )
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // 顶部取消按钮
            Text(
                text = stringResource(R.string.btn_cancel),
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier
                    .clickable { onNavigateBack() }
                    .padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // 标题
            Text(
                text = stringResource(R.string.meeting_share_screen),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 副标题
            Text(
                text = stringResource(R.string.msg_enter_meeting_code),
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 输入框
            OutlinedTextField(
                value = meetingId,
                onValueChange = {
                    meetingId = it
                    presenter.onMeetingIdChanged(it)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE0E0E0),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 开始共享按钮
            Button(
                onClick = { presenter.onStartShareClicked(meetingId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (meetingId.isNotBlank()) Color(0xFF1976D2) else Color(0xFF90CAF9),
                    disabledContainerColor = Color(0xFF90CAF9)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = meetingId.isNotBlank()
            ) {
                Text(
                    text = stringResource(R.string.btn_start_share),
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 底部帮助文本
            Text(
                text = stringResource(R.string.help_how_to_share_screen),
                fontSize = 14.sp,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
                    .clickable { /* 暂不处理 */ }
            )
        }

        // 错误提示
        errorMessage?.let { message ->
            LaunchedEffect(message) {
                errorMessage = null
            }
        }
    }
}
