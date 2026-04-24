package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R

@Composable
fun HistorySearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    OutlinedTextField(value = searchQuery, onValueChange = onSearchQueryChange, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), placeholder = { Text(text = stringResource(R.string.placeholder_meeting_search), fontSize = 14.sp, color = Color.Gray) }, leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.icon_desc_search), tint = Color.Gray) }, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF1976D2), unfocusedBorderColor = Color(0xFFE0E0E0), focusedContainerColor = Color.White, unfocusedContainerColor = Color.White), shape = RoundedCornerShape(8.dp), singleLine = true)
}
