package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.appsim.tencent_meeting_sim.data.model.User
import com.example.tencent_meeting_sim.R

/**
 * 参会人选择器对话框
 * 用于选择会议参会人员，支持全选功能
 */
@Composable
fun ParticipantPickerDialog(
    availableUsers: List<User>,
    selectedUsers: List<User>,
    onConfirm: (List<User>) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedList by remember { mutableStateOf(selectedUsers) }

    // 检查是否全选
    val isAllSelected = remember(selectedList, availableUsers) {
        selectedList.size == availableUsers.size && availableUsers.all { selectedList.contains(it) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_select_participants)) },
        text = {
            Column(
                modifier = Modifier
                    .height(300.dp)
                    .verticalScroll(rememberScrollState()) // 添加滚动功能
            ) {
                // 全选按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedList = if (isAllSelected) {
                                emptyList()
                            } else {
                                availableUsers
                            }
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isAllSelected,
                        onCheckedChange = { checked ->
                            selectedList = if (checked) {
                                availableUsers
                            } else {
                                emptyList()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.label_select_all), fontWeight = FontWeight.Medium)
                }

                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)

                // 用户列表
                availableUsers.forEach { user ->
                    val isSelected = selectedList.contains(user)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedList = if (isSelected) {
                                    selectedList - user
                                } else {
                                    selectedList + user
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { checked ->
                                selectedList = if (checked) {
                                    selectedList + user
                                } else {
                                    selectedList - user
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(user.username, fontWeight = FontWeight.Medium)
                            user.phone?.let { phone ->
                                Text(phone, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedList) }) {
                Text(stringResource(R.string.btn_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}
