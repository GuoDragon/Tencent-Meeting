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
 * 日期选择器对话框
 * 用于选择会议开始日期
 */
@Composable
fun DatePickerDialog(
    currentTime: Long,
    onConfirm: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = currentTime }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_select_date)) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 年份选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Icon(Icons.Default.Remove, stringResource(R.string.icon_desc_decrease_year))
                    }
                    Text(stringResource(R.string.label_year_with_value, selectedYear), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { selectedYear++ }) {
                        Icon(Icons.Default.Add, stringResource(R.string.icon_desc_increase_year))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 月份选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (selectedMonth > 0) selectedMonth--
                        else { selectedMonth = 11; selectedYear-- }
                    }) {
                        Icon(Icons.Default.Remove, stringResource(R.string.icon_desc_decrease_month))
                    }
                    Text(stringResource(R.string.label_month_with_value, selectedMonth + 1), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        if (selectedMonth < 11) selectedMonth++
                        else { selectedMonth = 0; selectedYear++ }
                    }) {
                        Icon(Icons.Default.Add, stringResource(R.string.icon_desc_increase_month))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 日期选择
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val maxDays = Calendar.getInstance().apply {
                        set(Calendar.YEAR, selectedYear)
                        set(Calendar.MONTH, selectedMonth)
                        set(Calendar.DAY_OF_MONTH, 1)
                    }.getActualMaximum(Calendar.DAY_OF_MONTH)

                    IconButton(onClick = {
                        if (selectedDay > 1) selectedDay--
                    }) {
                        Icon(Icons.Default.Remove, stringResource(R.string.icon_desc_decrease_day))
                    }
                    Text(stringResource(R.string.label_day_with_value, selectedDay), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = {
                        if (selectedDay < maxDays) selectedDay++
                    }) {
                        Icon(Icons.Default.Add, stringResource(R.string.icon_desc_increase_day))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newCalendar = Calendar.getInstance().apply {
                    timeInMillis = currentTime
                    set(Calendar.YEAR, selectedYear)
                    set(Calendar.MONTH, selectedMonth)
                    set(Calendar.DAY_OF_MONTH, selectedDay)
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
