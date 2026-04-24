package com.appsim.tencent_meeting_sim.presentation.meeting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencent_meeting_sim.R

/**
 * 搜索和邀请栏组件
 * 包含搜索框和邀请按钮
 */
@Composable
fun SearchAndInviteBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onInviteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 搜索框
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    text = stringResource(R.string.placeholder_search_members),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.icon_desc_search),
                    tint = Color.Gray
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            ),
            modifier = Modifier.weight(1f)
        )

        // 邀请按钮
        IconButton(
            onClick = onInviteClick,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = stringResource(R.string.btn_invite),
                tint = Color.Black
            )
        }
    }
}
