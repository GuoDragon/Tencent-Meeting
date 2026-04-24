package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R
import java.util.*

/**
 * 时间选择器对话框
 * 用于选择会议开始时间（小时和分钟）
 */
@Composable
fun TimePickerDialog(
    currentTime: Long,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }
    var selectedHour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_select_time)) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 小时选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            selectedHour = if (selectedHour > 0) selectedHour - 1 else 23
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Remove, stringResource(R.string.icon_desc_decrease_hour))
                    }
                    Text(String.format("%02d", selectedHour), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = {
                            selectedHour = if (selectedHour < 23) selectedHour + 1 else 0
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, stringResource(R.string.icon_desc_increase_hour))
                    }
                }

                Text(":", fontSize = 32.sp, fontWeight = FontWeight.Bold)

                // 分钟选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            selectedMinute = if (selectedMinute > 0) selectedMinute - 1 else 59
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Remove, stringResource(R.string.icon_desc_decrease_minute))
                    }
                    Text(String.format("%02d", selectedMinute), fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = {
                            selectedMinute = if (selectedMinute < 59) selectedMinute + 1 else 0
                        },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Add, stringResource(R.string.icon_desc_increase_minute))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newCalendar = Calendar.getInstance().apply {
                    timeInMillis = currentTime
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                    set(Calendar.SECOND, 0)
                }
                onConfirm(newCalendar.timeInMillis)
            }) {
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
