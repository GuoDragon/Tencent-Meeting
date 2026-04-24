package com.appsim.tencent_meeting_sim.presentation.contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.User
import com.appsim.tencent_meeting_sim.presentation.contact.components.InfoItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsDetailsScreen(userId: String, onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { FriendsDetailsPresenter(dataRepository) }
    var friendInfo by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showCallSuccessMessage by remember { mutableStateOf(false) }
    var callMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val view = remember {
        object : FriendsDetailsContract.View {
            override fun showFriendInfo(user: User) { friendInfo = user }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun showCallSuccess(friendName: String) { callMessage = context.getString(R.string.msg_calling, friendName); showCallSuccessMessage = true }
        }
    }

    LaunchedEffect(userId) { presenter.attachView(view); presenter.loadFriendInfoById(userId) }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Scaffold { paddingValues ->
        if (friendInfo == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                when {
                    isLoading -> CircularProgressIndicator()
                    errorMessage != null -> Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(text = errorMessage!!, color = Color.Red); Spacer(modifier = Modifier.height(16.dp)); Button(onClick = onNavigateBack) { Text(stringResource(R.string.btn_back)) } }
                    else -> CircularProgressIndicator()
                }
            }
        } else {
            val friend = friendInfo!!
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).verticalScroll(rememberScrollState())) {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFB0BEC5))) {
                    Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.Start) { IconButton(onClick = onNavigateBack) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.icon_desc_back), tint = Color.Black) } }
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 50.dp, bottom = 32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0xFF1976D2)), contentAlignment = Alignment.Center) { Text(text = friend.username.firstOrNull()?.toString() ?: "U", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold) }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = friend.username, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
                Column(modifier = Modifier.fillMaxWidth().background(Color.White).padding(16.dp)) {
                    InfoItem(label = stringResource(R.string.label_source), value = stringResource(R.string.msg_added_via_meeting), showArrow = true, onClick = { })
                    Divider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    friend.phone?.let { phone -> InfoItem(label = stringResource(R.string.label_contact_method), value = phone, showArrow = false, onClick = { }); Divider(color = Color(0xFFEEEEEE), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp)) }
                    friend.email?.let { email -> InfoItem(label = stringResource(R.string.label_email), value = email, showArrow = false, onClick = { }) }
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedButton(onClick = { presenter.callFriend(friend) }, modifier = Modifier.fillMaxWidth().height(48.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = Color.Black)) {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = stringResource(R.string.btn_call), tint = Color.Black, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.btn_call), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
        if (showCallSuccessMessage) {
            LaunchedEffect(Unit) { kotlinx.coroutines.delay(2000); showCallSuccessMessage = false }
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) { Card(modifier = Modifier.padding(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))) { Text(text = callMessage, color = Color.White, modifier = Modifier.padding(12.dp)) } }
        }
        errorMessage?.let { message -> LaunchedEffect(message) { kotlinx.coroutines.delay(2000); errorMessage = null } }
    }
}
