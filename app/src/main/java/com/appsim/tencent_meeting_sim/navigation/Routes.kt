package com.appsim.tencent_meeting_sim.navigation

/**
 * Navigation route definitions for the application
 * Uses sealed class for type safety and compile-time checking
 */
sealed class Screen(val route: String) {
    // Bottom Navigation Tabs (Base routes without parameters)
    object Home : Screen("home")
    object Contact : Screen("contact")
    object Me : Screen("me")

    // Meeting Screens (Full-screen overlays)
    object ScheduledMeeting : Screen("scheduled_meeting")
    object JoinMeeting : Screen("join_meeting")
    object QuickMeeting : Screen("quick_meeting")
    object ShareScreenInput : Screen("share_screen_input")
    object HistoryMeetings : Screen("history_meetings")

    // Meeting Details (requires meetingId + boolean flags)
    object MeetingDetails : Screen("meeting_details/{meetingId}?micEnabled={micEnabled}&videoEnabled={videoEnabled}&speakerEnabled={speakerEnabled}&recordingEnabled={recordingEnabled}&screenSharing={screenSharing}") {
        fun createRoute(
            meetingId: String,
            micEnabled: Boolean = false,
            videoEnabled: Boolean = false,
            speakerEnabled: Boolean = true,
            recordingEnabled: Boolean = false,
            screenSharing: Boolean = false
        ) = "meeting_details/$meetingId?micEnabled=$micEnabled&videoEnabled=$videoEnabled&speakerEnabled=$speakerEnabled&recordingEnabled=$recordingEnabled&screenSharing=$screenSharing"
    }

    // Meeting Chat (requires meetingId)
    object MeetingChat : Screen("meeting_chat/{meetingId}") {
        fun createRoute(meetingId: String) = "meeting_chat/$meetingId"
    }

    // Meeting Replay (requires meetingId)
    object MeetingReplay : Screen("meeting_replay/{meetingId}") {
        fun createRoute(meetingId: String) = "meeting_replay/$meetingId"
    }

    // Scheduled Meeting Details (requires meetingId)
    object ScheduledMeetingDetails : Screen("scheduled_meeting_details/{meetingId}") {
        fun createRoute(meetingId: String) = "scheduled_meeting_details/$meetingId"
    }

    // Contact Screens
    object AddFriends : Screen("add_friends")

    // Friends Details (complex object - use savedStateHandle)
    object FriendsDetails : Screen("friends_details/{userId}") {
        fun createRoute(userId: String) = "friends_details/$userId"
    }

    // Me/Profile Screens
    object PersonalMeetingRoom : Screen("personal_meeting_room")
    object PersonalInformation : Screen("personal_information")
    object Record : Screen("record")
}

/**
 * Navigation arguments keys
 */
object NavArgs {
    const val MEETING_ID = "meetingId"
    const val USER_ID = "userId"
    const val MIC_ENABLED = "micEnabled"
    const val VIDEO_ENABLED = "videoEnabled"
    const val SPEAKER_ENABLED = "speakerEnabled"
    const val RECORDING_ENABLED = "recordingEnabled"
    const val SCREEN_SHARING = "screenSharing"
}
