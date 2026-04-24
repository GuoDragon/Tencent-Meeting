package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.appsim.tencent_meeting_sim.data.model.User
import com.example.tencent_meeting_sim.R

/**
 * 邀请联系人对话框
 * 用于选择并邀请联系人加入会议
 */
@Composable
fun InviteContactDialog(
    contacts: List<User>,
    selectedContacts: Set<String>,
    onContactToggle: (String) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onInvite: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean = false
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .fillMaxHeight(0.7f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                // 顶部标题栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.btn_invite_new_member),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 全选/取消全选按钮
                        TextButton(
                            onClick = {
                                if (selectedContacts.size == contacts.size) {
                                    onDeselectAll()
                                } else {
                                    onSelectAll()
                                }
                            }
                        ) {
                            Text(
                                text = if (selectedContacts.size == contacts.size) "取消全选" else "全选",
                                fontSize = 14.sp,
                                color = Color(0xFF1976D2)
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.icon_desc_close),
                                tint = Color.Gray
                            )
                        }
                    }
                }

                // 联系人列表
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(contacts) { contact ->
                        ContactSelectionItem(
                            contact = contact,
                            isSelected = selectedContacts.contains(contact.userId),
                            onToggle = { onContactToggle(contact.userId) }
                        )
                    }
                }

                // 底部按钮
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.btn_cancel),
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = onInvite,
                        enabled = selectedContacts.isNotEmpty() && !isLoading,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1976D2)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.btn_invite_count, selectedContacts.size),
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 联系人选择项组件（内部使用）
 */
@Composable
private fun ContactSelectionItem(
    contact: User,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = contact.username.firstOrNull()?.toString() ?: "U",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contact.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                contact.phone?.let { phone ->
                    Text(
                        text = phone,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
