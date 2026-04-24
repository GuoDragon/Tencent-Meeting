package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 屏幕共享视图组件
 * 模拟显示手机桌面屏幕
 */
@Composable
fun ScreenShareView() {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(9f / 19f)
            .clip(RoundedCornerShape(32.dp))
            .border(8.dp, Color(0xFF2C2C2E), RoundedCornerShape(32.dp))
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF5D7A9E),
                            Color(0xFF2C3E50),
                            Color(0xFF1A1A2E)
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                ScreenStatusBar()
                Spacer(modifier = Modifier.weight(1f))
                ScreenAppIcons()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ScreenStatusBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 24.dp)
    ) {
        Text("6:59", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f), fontWeight = FontWeight.Normal)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Thu, Oct 23", fontSize = 12.sp, color = Color.White.copy(alpha = 0.7f), fontWeight = FontWeight.Light)
    }
}

@Composable
private fun ScreenAppIcons() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            AppIcon(Icons.Default.Email, "Gmail")
            AppIcon(Icons.Default.PhotoLibrary, "Photos")
            AppIcon(Icons.Default.PlayCircle, "YouTube")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            AppIcon(Icons.Default.Phone, "Phone")
            AppIcon(Icons.Default.Message, "Messages")
            Spacer(modifier = Modifier.width(64.dp))
            AppIcon(Icons.Default.Email, "Gmail")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.Search, "Search", tint = Color(0xFF5F6368), modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.Mic, "Voice", tint = Color(0xFF4285F4), modifier = Modifier.size(24.dp))
                Icon(Icons.Default.Camera, "Camera", tint = Color(0xFF4285F4), modifier = Modifier.size(24.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .width(120.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White.copy(alpha = 0.6f))
        )
    }
}

@Composable
private fun AppIcon(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(72.dp)) {
        Box(
            modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, label, tint = Color(0xFF4285F4), modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(label, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Normal)
    }
}
