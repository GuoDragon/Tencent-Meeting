package com.appsim.tencent_meeting_sim.presentation.contact

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val dataRepository = DataRepository.getInstance(context)
    val presenter = remember { AddFriendsPresenter(dataRepository) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showCopySuccessMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val inviteLink = "https://meeting.tencent.com/j/diQZucKlS9h"

    val view = remember {
        object : AddFriendsContract.View {
            override fun showUserInfo(user: User) { currentUser = user }
            override fun showLoading() { isLoading = true }
            override fun hideLoading() { isLoading = false }
            override fun showError(message: String) { errorMessage = message }
            override fun showCopySuccess() { showCopySuccessMessage = true }
        }
    }

    LaunchedEffect(Unit) { presenter.attachView(view); presenter.loadUserInfo() }
    DisposableEffect(Unit) { onDispose { presenter.onDestroy() } }

    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.contact_add), fontSize = 18.sp, fontWeight = FontWeight.Medium) }, navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.icon_desc_back), tint = Color.Black) } }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFE3F2FD))) }) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).background(brush = Brush.verticalGradient(colors = listOf(Color(0xFFE3F2FD), Color(0xFFECEFF1)))).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0xFF1976D2)), contentAlignment = Alignment.Center) { Text(text = "刘", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold) }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "刘承龙", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(Color.Gray), contentAlignment = Alignment.Center) { Text(text = "0", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.label_free_version), fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)) {
                Text(text = stringResource(R.string.contact_invite_template, "刘承龙", inviteLink), fontSize = 14.sp, color = Color.Black, modifier = Modifier.padding(16.dp), lineHeight = 20.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.msg_max_contacts_limit), fontSize = 12.sp, color = Color.Gray)
                Text(text = stringResource(R.string.btn_reset), fontSize = 12.sp, color = Color(0xFF1976D2), modifier = Modifier.clickable { })
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager; val clip = ClipData.newPlainText("invite_link", inviteLink); clipboard.setPrimaryClip(clip); presenter.copyLink(inviteLink) }, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(48.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))) {
                Text(text = stringResource(R.string.btn_copy_link), fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(onClick = { presenter.shareToWeChat() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(48.dp), colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = Color.Black)) {
                Text(text = stringResource(R.string.btn_share_wechat), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        if (showCopySuccessMessage) {
            LaunchedEffect(Unit) { kotlinx.coroutines.delay(2000); showCopySuccessMessage = false }
            Snackbar(modifier = Modifier.padding(16.dp), action = { TextButton(onClick = { showCopySuccessMessage = false }) { Text(stringResource(R.string.btn_confirm), color = Color(0xFF1976D2)) } }) { Text(stringResource(R.string.msg_link_copied)) }
        }
        errorMessage?.let { message -> LaunchedEffect(message) { kotlinx.coroutines.delay(2000); errorMessage = null } }
    }
}
