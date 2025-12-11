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
import com.appsim.tencent_meeting_sim.data.model.RecurrenceType
import com.example.tencent_meeting_sim.R

/**
 * 重复频率选择器对话框
 * 用于选择会议重复类型（NONE, DAILY, WEEKLY, MONTHLY）
 */
@Composable
fun RecurrencePickerDialog(
    currentRecurrence: RecurrenceType,
    onConfirm: (RecurrenceType) -> Unit,
    onDismiss: () -> Unit
) {
    val recurrenceTypes = RecurrenceType.values()
    var selectedRecurrence by remember { mutableStateOf(currentRecurrence) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_select_recurrence)) },
        text = {
            Column {
                recurrenceTypes.forEach { recurrence ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedRecurrence = recurrence }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRecurrence == recurrence,
                            onClick = { selectedRecurrence = recurrence }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(recurrence.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedRecurrence) }) {
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
