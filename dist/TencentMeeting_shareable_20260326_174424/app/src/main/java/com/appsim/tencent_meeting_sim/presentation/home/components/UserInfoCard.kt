package com.appsim.tencent_meeting_sim.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import com.appsim.tencent_meeting_sim.data.model.User

@Composable
fun UserInfoCard(user: User, onUserInfoClick: () -> Unit = {}) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onUserInfoClick() }, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(Color(0xFF1976D2)), contentAlignment = Alignment.Center) {
                    Text(text = "刘", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "刘承龙", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = user.phone ?: stringResource(R.string.user_not_set_phone), fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "liuchenglong@example.com", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}
