package com.appsim.tencent_meeting_sim.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.net.URLDecoder

/**
 * 通用占位页面 - 用于显示未实现的功能
 *
 * @param featureName 功能名称（可能是URL编码的）
 * @param onNavigateBack 返回按钮点击回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderScreen(
    featureName: String,
    onNavigateBack: () -> Unit
) {
    // URL解码功能名称
    val decodedFeatureName = try {
        URLDecoder.decode(featureName, "UTF-8")
    } catch (e: Exception) {
        featureName
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFF5F5F5))
                )
            )
    ) {
        // 顶部导航栏
        TopAppBar(
            title = {
                Text(
                    text = decodedFeatureName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "返回",
                        tint = Color.Black
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFE3F2FD)
            ),
            modifier = Modifier.padding(top = 24.dp)
        )

        // 中央内容区域
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 施工图标
            Icon(
                imageVector = Icons.Default.Construction,
                contentDescription = "功能开发中",
                tint = Color(0xFF1976D2),
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 标题
            Text(
                text = "功能开发中",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 描述文字
            Text(
                text = "该功能正在开发中，敬请期待",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 功能名称显示
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = decodedFeatureName,
                    fontSize = 18.sp,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }
    }
}
