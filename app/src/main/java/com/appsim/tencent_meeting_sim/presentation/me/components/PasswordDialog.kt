package com.appsim.tencent_meeting_sim.presentation.me.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.tencent_meeting_sim.R

/**
 * 密码设置对话框
 * 支持启用/禁用密码和设置6位数字密码
 */
@Composable
fun PasswordDialog(
    currentPassword: String,
    currentEnabled: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, Boolean) -> Unit
) {
    var password by remember { mutableStateOf(currentPassword) }
    var enabled by remember { mutableStateOf(currentEnabled) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                // 顶部栏
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.btn_cancel), color = Color(0xFF1976D2))
                    }

                    Text(text = stringResource(R.string.meeting_password), fontSize = 18.sp, fontWeight = FontWeight.Bold)

                    TextButton(onClick = { onSave(password, enabled) }) {
                        Text(stringResource(R.string.btn_save), color = Color(0xFF1976D2))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 启用开关
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.meeting_password), fontSize = 16.sp)
                    Switch(checked = enabled, onCheckedChange = { enabled = it })
                }

                if (enabled) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // 密码输入框
                    OutlinedTextField(
                        value = password,
                        onValueChange = { if (it.length <= 6 && it.all { char -> char.isDigit() }) password = it },
                        label = { Text(stringResource(R.string.label_password_6_digits)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        }
    }
}
