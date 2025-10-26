package com.example.tencentmeeting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tencentmeeting.ui.theme.TencentMeetingTheme
import com.example.tencentmeeting.view.HomePage
import com.example.tencentmeeting.view.MePage
import com.example.tencentmeeting.view.ContactPage
import com.example.tencentmeeting.view.ScheduledMeetingPage
import com.example.tencentmeeting.view.AddFriendsPage
import com.example.tencentmeeting.view.FriendsDetailsPage
import com.example.tencentmeeting.view.JoinMeetingPage
import com.example.tencentmeeting.view.QuickMeetingPage
import com.example.tencentmeeting.view.MeetingDetailsPage
import com.example.tencentmeeting.view.MeetingChatPage
import com.example.tencentmeeting.view.MeetingReplayPage
import com.example.tencentmeeting.model.User

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TencentMeetingTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    var showScheduledMeetingPage by remember { mutableStateOf(false) }
    var showAddFriendsPage by remember { mutableStateOf(false) }
    var showFriendsDetailsPage by remember { mutableStateOf(false) }
    var showJoinMeetingPage by remember { mutableStateOf(false) }
    var showQuickMeetingPage by remember { mutableStateOf(false) }
    var showMeetingDetailsPage by remember { mutableStateOf(false) }
    var showMeetingChatPage by remember { mutableStateOf(false) }
    var showMeetingReplayPage by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<User?>(null) }
    var currentMeetingId by remember { mutableStateOf("") }
    var currentChatMeetingId by remember { mutableStateOf("") }
    var replayMeetingId by remember { mutableStateOf("") }
    var initialMicEnabled by remember { mutableStateOf(true) }
    var initialVideoEnabled by remember { mutableStateOf(false) }
    var initialSpeakerEnabled by remember { mutableStateOf(true) }

    if (showScheduledMeetingPage) {
        // 显示预定会议页面
        ScheduledMeetingPage(
            onNavigateBack = { showScheduledMeetingPage = false },
            onNavigateToMeetingDetails = { meetingId ->
                currentMeetingId = meetingId
                showScheduledMeetingPage = false
                showMeetingDetailsPage = true
            }
        )
    } else if (showAddFriendsPage) {
        // 显示添加联系人页面
        AddFriendsPage(
            onNavigateBack = { showAddFriendsPage = false }
        )
    } else if (showFriendsDetailsPage && selectedContact != null) {
        // 显示好友详情页面
        FriendsDetailsPage(
            friend = selectedContact!!,
            onNavigateBack = { showFriendsDetailsPage = false }
        )
    } else if (showJoinMeetingPage) {
        // 显示加入会议页面
        JoinMeetingPage(
            onNavigateBack = { showJoinMeetingPage = false },
            onNavigateToMeetingDetails = { meetingId, micEnabled, videoEnabled, speakerEnabled ->
                currentMeetingId = meetingId
                initialMicEnabled = micEnabled
                initialVideoEnabled = videoEnabled
                initialSpeakerEnabled = speakerEnabled
                showJoinMeetingPage = false
                showMeetingDetailsPage = true
            }
        )
    } else if (showQuickMeetingPage) {
        // 显示快速会议页面
        QuickMeetingPage(
            onNavigateBack = { showQuickMeetingPage = false },
            onNavigateToMeetingDetails = { meetingId, micEnabled, videoEnabled, speakerEnabled ->
                currentMeetingId = meetingId
                initialMicEnabled = micEnabled
                initialVideoEnabled = videoEnabled
                initialSpeakerEnabled = speakerEnabled
                showQuickMeetingPage = false
                showMeetingDetailsPage = true
            }
        )
    } else if (showMeetingChatPage) {
        // 显示会议聊天页面
        MeetingChatPage(
            meetingId = currentChatMeetingId,
            onClose = {
                showMeetingChatPage = false
                showMeetingDetailsPage = true
            }
        )
    } else if (showMeetingReplayPage) {
        // 显示会议回放页面
        MeetingReplayPage(
            meetingId = replayMeetingId,
            onNavigateBack = { showMeetingReplayPage = false }
        )
    } else if (showMeetingDetailsPage) {
        // 显示会议详情页面
        MeetingDetailsPage(
            meetingId = currentMeetingId,
            initialMicEnabled = initialMicEnabled,
            initialVideoEnabled = initialVideoEnabled,
            initialSpeakerEnabled = initialSpeakerEnabled,
            onNavigateBack = { showMeetingDetailsPage = false },
            onNavigateToChatPage = {
                currentChatMeetingId = currentMeetingId
                showMeetingDetailsPage = false
                showMeetingChatPage = true
            }
        )
    } else {
        // 显示主页面
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> HomePage(
                        onNavigateToScheduledMeeting = { showScheduledMeetingPage = true },
                        onNavigateToJoinMeeting = { showJoinMeetingPage = true },
                        onNavigateToQuickMeeting = { showQuickMeetingPage = true },
                        onNavigateToMeetingDetails = { meetingId ->
                            currentMeetingId = meetingId
                            showMeetingDetailsPage = true
                        }
                    )
                    1 -> ContactPage(
                        onNavigateToAddFriends = { showAddFriendsPage = true },
                        onNavigateToFriendDetails = { contact ->
                            selectedContact = contact
                            showFriendsDetailsPage = true
                        }
                    )
                    2 -> MePage(
                        onMeetingClick = { meetingId ->
                            replayMeetingId = meetingId
                            showMeetingReplayPage = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.VideoCall,
                    contentDescription = "会议"
                )
            },
            label = {
                Text(
                    text = "会议",
                    fontSize = 12.sp
                )
            },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                selectedTextColor = Color(0xFF1976D2),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Contacts,
                    contentDescription = "通讯录"
                )
            },
            label = {
                Text(
                    text = "通讯录",
                    fontSize = 12.sp
                )
            },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                selectedTextColor = Color(0xFF1976D2),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "我的"
                )
            },
            label = {
                Text(
                    text = "我的",
                    fontSize = 12.sp
                )
            },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF1976D2),
                selectedTextColor = Color(0xFF1976D2),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}



@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TencentMeetingTheme {
        MainScreen()
    }
}