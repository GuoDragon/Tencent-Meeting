package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tencent_meeting_sim.R

@Composable
fun MeetingChatInputBar(inputText: String, onInputChange: (String) -> Unit, onSend: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 8.dp, color = Color.White) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(value = inputText, onValueChange = onInputChange, modifier = Modifier.weight(1f).padding(end = 8.dp), placeholder = { Text(stringResource(R.string.placeholder_enter_message)) }, maxLines = 3, shape = RoundedCornerShape(20.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1976D2), unfocusedBorderColor = Color.LightGray))
            IconButton(onClick = { if (inputText.isNotBlank()) onSend() }, modifier = Modifier.size(48.dp).background(color = if (inputText.isNotBlank()) Color(0xFF1976D2) else Color.LightGray, shape = RoundedCornerShape(24.dp))) {
                Icon(imageVector = Icons.Default.Send, contentDescription = stringResource(R.string.icon_desc_send), tint = Color.White)
            }
        }
    }
}
