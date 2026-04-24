package com.appsim.tencent_meeting_sim.presentation.contact.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoItem(label: String, value: String, showArrow: Boolean, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, fontSize = 14.sp, color = Color.Black)
            if (showArrow) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}
