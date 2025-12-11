package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingChatTopBar(onClose: () -> Unit) {
    TopAppBar(title = { Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text(text = stringResource(R.string.meeting_chat), fontSize = 18.sp, fontWeight = FontWeight.Bold) } }, actions = { IconButton(onClick = onClose) { Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.icon_desc_close)) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE3F2FD), titleContentColor = Color.Black, actionIconContentColor = Color.Black))
}
