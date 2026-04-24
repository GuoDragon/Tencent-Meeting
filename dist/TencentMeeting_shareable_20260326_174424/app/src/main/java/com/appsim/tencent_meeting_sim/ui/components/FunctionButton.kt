package com.appsim.tencent_meeting_sim.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 通用功能按钮组件
 * 垂直布局：图标 + 文字
 */
@Composable
fun FunctionButton(
    icon: ImageVector,
    text: String,
    iconTint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = text,
            fontSize = 11.sp,
            color = Color.White
        )
    }
}
