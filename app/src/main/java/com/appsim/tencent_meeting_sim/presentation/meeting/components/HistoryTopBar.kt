package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R

@Composable
fun HistoryTopBar(onNavigateBack: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFE3F2FD)).padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.icon_desc_back), modifier = Modifier.size(24.dp).clickable { onNavigateBack() }, tint = Color.Black)
        Text(text = stringResource(R.string.meeting_history), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(R.string.icon_desc_more), modifier = Modifier.size(24.dp), tint = Color.Black)
    }
}
