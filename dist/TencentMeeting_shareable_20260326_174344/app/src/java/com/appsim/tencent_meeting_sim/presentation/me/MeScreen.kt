package com.appsim.tencent_meeting_sim.presentation.me

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
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
import com.appsim.tencent_meeting_sim.presentation.me.MeContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.me.MePresenter

@Composable
fun MeScreen(onMeetingClick: (String) -> Unit = {}, onPersonalMeetingRoomClick: () -> Unit = {}, onPersonalInfoClick: () -> Unit = {}, onRecordClick: () -> Unit = {}, onNavigateToPlaceholder: (String) -> Unit = {}) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { MePresenter(dataRepository) }
    var currentUser by remember { mutableStateOf<User?>(null) }

    val view = remember {
        object : MeContract.View {
            override fun showUserInfo(user: User) { currentUser = user }
            override fun showHistoryMeetings(meetings: List<Meeting>) { }
            override fun showEmptyHistory() { }
            override fun showLoading() { }
            override fun hideLoading() { }
            override fun showError(message: String) { }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadUserInfo() }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Column(modifier = Modifier.fillMaxSize().background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5)))).verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onPersonalInfoClick() }, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFF1976D2)), contentAlignment = Alignment.Center) {
                        Text(text = "刘", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "刘承龙", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = currentUser?.phone ?: stringResource(R.string.user_not_set_phone), fontSize = 16.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "liuchenglong@example.com", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), shape = RoundedCornerShape(12.dp)) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f).clickable(onClick = onPersonalMeetingRoomClick).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = stringResource(R.string.user_personal_room), tint = Color(0xFF1976D2), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.user_personal_room), fontSize = 13.sp, color = Color.Black)
                    }
                    Column(modifier = Modifier.weight(1f).clickable(onClick = onRecordClick).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Videocam, contentDescription = stringResource(R.string.me_recording), tint = Color(0xFF1976D2), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.me_recording), fontSize = 13.sp, color = Color.Black)
                    }
                    Column(modifier = Modifier.weight(1f).clickable(onClick = { onNavigateToPlaceholder("我的笔记") }).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.Note, contentDescription = stringResource(R.string.me_notes), tint = Color(0xFF1976D2), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.me_notes), fontSize = 13.sp, color = Color.Black)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f).clickable(onClick = { onNavigateToPlaceholder("AI助手") }).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.SmartToy, contentDescription = stringResource(R.string.me_ai_assistant), tint = Color(0xFF1976D2), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.me_ai_assistant), fontSize = 13.sp, color = Color.Black)
                    }
                    Column(modifier = Modifier.weight(1f).clickable(onClick = { onNavigateToPlaceholder("订单与服务") }).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = stringResource(R.string.me_orders_service), tint = Color(0xFF1976D2), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.me_orders_service), fontSize = 13.sp, color = Color.Black)
                    }
                    Column(modifier = Modifier.weight(1f).clickable(onClick = { onNavigateToPlaceholder("控制室") }).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(imageVector = Icons.Default.MeetingRoom, contentDescription = stringResource(R.string.me_control_rooms), tint = Color(0xFF1976D2), modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(R.string.me_control_rooms), fontSize = 13.sp, color = Color.Black)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            listOf(
                Icons.Default.Stars to stringResource(R.string.me_points_center),
                Icons.Default.Security to stringResource(R.string.me_account_security),
                Icons.Default.Settings to stringResource(R.string.me_settings),
                Icons.Default.Lock to stringResource(R.string.me_privacy),
                Icons.Default.Help to stringResource(R.string.me_help),
                Icons.Default.Info to stringResource(R.string.me_about)
            ).forEach { (icon, text) ->
                Card(modifier = Modifier.fillMaxWidth().clickable(onClick = { onNavigateToPlaceholder(text) }), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp), shape = RoundedCornerShape(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = icon, contentDescription = text, tint = Color.Gray, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = text, fontSize = 15.sp, color = Color.Black, modifier = Modifier.weight(1f))
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = stringResource(R.string.action_enter), tint = Color.Gray, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { onNavigateToPlaceholder("退出登录") }, contentAlignment = Alignment.Center) {
            Text(text = stringResource(R.string.btn_logout), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE53935))
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
