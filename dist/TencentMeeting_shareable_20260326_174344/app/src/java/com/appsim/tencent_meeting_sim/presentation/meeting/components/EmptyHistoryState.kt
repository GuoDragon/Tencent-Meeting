package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R

@Composable
fun EmptyHistoryState() {
    Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(imageVector = Icons.Default.History, contentDescription = stringResource(R.string.msg_empty_history_meetings), tint = Color.Gray, modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = stringResource(R.string.msg_empty_history_meetings), fontSize = 16.sp, color = Color.Gray)
    }
}
