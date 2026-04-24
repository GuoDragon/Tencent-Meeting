package com.appsim.tencent_meeting_sim.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.appsim.tencent_meeting_sim.presentation.home.HomeScreen
import com.appsim.tencent_meeting_sim.presentation.contact.ContactScreen
import com.appsim.tencent_meeting_sim.presentation.me.MeScreen
import com.appsim.tencent_meeting_sim.presentation.meeting.*
import com.appsim.tencent_meeting_sim.presentation.contact.AddFriendsScreen
import com.appsim.tencent_meeting_sim.presentation.contact.FriendsDetailsScreen
import com.appsim.tencent_meeting_sim.presentation.me.*

/**
 * Main navigation graph for the Tencent Meeting application
 * Manages all screen destinations and navigation between them
 */
@Composable
fun TencentMeetingNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onTabSelected: (Int) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        // ===== Bottom Navigation Tabs =====

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToScheduledMeeting = {
                    navController.navigate(Screen.ScheduledMeeting.route)
                },
                onNavigateToJoinMeeting = {
                    navController.navigate(Screen.JoinMeeting.route)
                },
                onNavigateToQuickMeeting = {
                    navController.navigate(Screen.QuickMeeting.route)
                },
                onNavigateToMeetingDetails = { meetingId ->
                    navController.navigate(Screen.MeetingDetails.createRoute(meetingId))
                },
                onNavigateToScheduledMeetingDetails = { meetingId ->
                    navController.navigate(Screen.ScheduledMeetingDetails.createRoute(meetingId))
                },
                onNavigateToHistoryMeetings = {
                    navController.navigate(Screen.HistoryMeetings.route)
                },
                onNavigateToShareScreen = {
                    navController.navigate(Screen.ShareScreenInput.route)
                },
                onNavigateToMeTab = { onTabSelected(2) }
            )
        }

        composable(Screen.Contact.route) {
            ContactScreen(
                onNavigateToAddFriends = {
                    navController.navigate(Screen.AddFriends.route)
                },
                onNavigateToFriendDetails = { user ->
                    navController.navigate(Screen.FriendsDetails.createRoute(user.userId))
                }
            )
        }

        composable(Screen.Me.route) {
            MeScreen(
                onMeetingClick = { meetingId ->
                    navController.navigate(Screen.MeetingReplay.createRoute(meetingId))
                },
                onPersonalMeetingRoomClick = {
                    navController.navigate(Screen.PersonalMeetingRoom.route)
                },
                onPersonalInfoClick = {
                    navController.navigate(Screen.PersonalInformation.route)
                },
                onRecordClick = {
                    navController.navigate(Screen.Record.route)
                },
                onNavigateToPlaceholder = { featureName ->
                    navController.navigate(Screen.Placeholder.createRoute(featureName))
                }
            )
        }

        // ===== Meeting Screens =====

        composable(Screen.ScheduledMeeting.route) {
            ScheduledMeetingScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMeetingDetails = { meetingId ->
                    navController.navigate(Screen.MeetingDetails.createRoute(meetingId))
                }
            )
        }

        composable(Screen.JoinMeeting.route) {
            JoinMeetingScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMeetingDetails = { meetingId, micEnabled, videoEnabled, speakerEnabled, recordingEnabled ->
                    navController.navigate(
                        Screen.MeetingDetails.createRoute(
                            meetingId = meetingId,
                            micEnabled = micEnabled,
                            videoEnabled = videoEnabled,
                            speakerEnabled = speakerEnabled,
                            recordingEnabled = recordingEnabled
                        )
                    )
                }
            )
        }

        composable(Screen.QuickMeeting.route) {
            QuickMeetingScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMeetingDetails = { meetingId, micEnabled, videoEnabled, speakerEnabled, recordingEnabled ->
                    navController.navigate(
                        Screen.MeetingDetails.createRoute(
                            meetingId = meetingId,
                            micEnabled = micEnabled,
                            videoEnabled = videoEnabled,
                            speakerEnabled = speakerEnabled,
                            recordingEnabled = recordingEnabled
                        )
                    )
                }
            )
        }

        composable(Screen.ShareScreenInput.route) {
            ShareScreenInputScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMeetingDetails = { meetingId, enableScreenSharing ->
                    navController.navigate(
                        Screen.MeetingDetails.createRoute(
                            meetingId = meetingId,
                            screenSharing = enableScreenSharing
                        )
                    )
                }
            )
        }

        composable(Screen.HistoryMeetings.route) {
            HistoryMeetingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMeetingReplay = { meetingId ->
                    navController.navigate(Screen.MeetingReplay.createRoute(meetingId))
                }
            )
        }

        // MeetingDetails with all parameters
        composable(
            route = Screen.MeetingDetails.route,
            arguments = listOf(
                navArgument(NavArgs.MEETING_ID) { type = NavType.StringType },
                navArgument(NavArgs.MIC_ENABLED) {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(NavArgs.VIDEO_ENABLED) {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(NavArgs.SPEAKER_ENABLED) {
                    type = NavType.BoolType
                    defaultValue = true
                },
                navArgument(NavArgs.RECORDING_ENABLED) {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument(NavArgs.SCREEN_SHARING) {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString(NavArgs.MEETING_ID) ?: ""
            val micEnabled = backStackEntry.arguments?.getBoolean(NavArgs.MIC_ENABLED) ?: false
            val videoEnabled = backStackEntry.arguments?.getBoolean(NavArgs.VIDEO_ENABLED) ?: false
            val speakerEnabled = backStackEntry.arguments?.getBoolean(NavArgs.SPEAKER_ENABLED) ?: true
            val recordingEnabled = backStackEntry.arguments?.getBoolean(NavArgs.RECORDING_ENABLED) ?: false
            val screenSharing = backStackEntry.arguments?.getBoolean(NavArgs.SCREEN_SHARING) ?: false

            MeetingDetailsScreen(
                meetingId = meetingId,
                initialMicEnabled = micEnabled,
                initialVideoEnabled = videoEnabled,
                initialSpeakerEnabled = speakerEnabled,
                initialRecordingEnabled = recordingEnabled,
                initialScreenSharing = screenSharing,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChatPage = {
                    navController.navigate(Screen.MeetingChat.createRoute(meetingId))
                }
            )
        }

        composable(
            route = Screen.MeetingChat.route,
            arguments = listOf(
                navArgument(NavArgs.MEETING_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString(NavArgs.MEETING_ID) ?: ""
            MeetingChatScreen(
                meetingId = meetingId,
                onClose = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MeetingReplay.route,
            arguments = listOf(
                navArgument(NavArgs.MEETING_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString(NavArgs.MEETING_ID) ?: ""
            MeetingReplayScreen(
                meetingId = meetingId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ScheduledMeetingDetails.route,
            arguments = listOf(
                navArgument(NavArgs.MEETING_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString(NavArgs.MEETING_ID) ?: ""
            ScheduledMeetingDetailsScreen(
                meetingId = meetingId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMeetingDetails = { meetingId ->
                    navController.navigate(Screen.MeetingDetails.createRoute(meetingId))
                }
            )
        }

        // ===== Contact Screens =====

        composable(Screen.AddFriends.route) {
            AddFriendsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.FriendsDetails.route,
            arguments = listOf(
                navArgument(NavArgs.USER_ID) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString(NavArgs.USER_ID) ?: ""
            FriendsDetailsScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ===== Me/Profile Screens =====

        composable(Screen.PersonalMeetingRoom.route) {
            PersonalMeetingRoomScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMeetingDetails = { meetingId ->
                    navController.navigate(Screen.MeetingDetails.createRoute(meetingId))
                },
                onNavigateToPlaceholder = { featureName ->
                    navController.navigate(Screen.Placeholder.createRoute(featureName))
                }
            )
        }

        composable(Screen.PersonalInformation.route) {
            PersonalInformationScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlaceholder = { featureName ->
                    navController.navigate(Screen.Placeholder.createRoute(featureName))
                }
            )
        }

        composable(Screen.Record.route) {
            RecordScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPlaceholder = { featureName ->
                    navController.navigate(Screen.Placeholder.createRoute(featureName))
                }
            )
        }

        // ===== Placeholder Screen =====

        composable(
            route = Screen.Placeholder.route,
            arguments = listOf(
                navArgument(NavArgs.FEATURE_NAME) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val featureName = backStackEntry.arguments?.getString(NavArgs.FEATURE_NAME) ?: ""
            com.appsim.tencent_meeting_sim.presentation.common.PlaceholderScreen(
                featureName = featureName,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
