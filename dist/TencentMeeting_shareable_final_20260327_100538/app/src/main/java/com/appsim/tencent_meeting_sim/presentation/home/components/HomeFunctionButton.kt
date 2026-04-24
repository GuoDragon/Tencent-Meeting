package com.appsim.tencent_meeting_sim.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeFunctionButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Card(modifier = Modifier.size(64.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)), shape = RoundedCornerShape(16.dp)) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = text, tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text, fontSize = 12.sp, color = Color.Black, textAlign = TextAlign.Center)
    }
}
