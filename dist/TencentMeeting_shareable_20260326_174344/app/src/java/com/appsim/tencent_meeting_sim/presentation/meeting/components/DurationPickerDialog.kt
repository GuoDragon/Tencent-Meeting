package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tencent_meeting_sim.R

/**
 * 会议时长选择器对话框
 * 提供预设的时长选项（15, 30, 45, 60, 90, 120分钟）
 */
@Composable
fun DurationPickerDialog(
    currentDuration: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val durations = listOf(15, 30, 45, 60, 90, 120)
    var selectedDuration by remember { mutableStateOf(currentDuration) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_select_duration)) },
        text = {
            Column {
                durations.forEach { duration ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedDuration = duration }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedDuration == duration,
                            onClick = { selectedDuration = duration }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.label_minutes_with_value, duration))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedDuration) }) {
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
