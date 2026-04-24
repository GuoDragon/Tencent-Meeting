package com.appsim.tencent_meeting_sim.common.constants

/**
 * 应用常量定义
 */
object AppConstants {
    const val APP_NAME = "TencentMeeting"
    const val PACKAGE_NAME = "com.appsim.tencent_meeting_sim"

    /**
     * 数据文件路径常量
     */
    object DataFiles {
        const val USERS = "data/users.json"
        const val MEETINGS = "data/meetings.json"
        const val MESSAGES = "data/messages.json"
        const val MEETING_PARTICIPANTS = "data/meeting_participants.json"
        const val HAND_RAISE_RECORDS = "data/hand_raise_records.json"
        const val MEETING_INVITATIONS = "data/meeting_invitations.json"
        const val PERSONAL_MEETING_ROOMS = "data/personal_meeting_rooms.json"
    }

    /**
     * 导航路由常量
     */
    object Routes {
        const val HOME = "home"
        const val CONTACT = "contact"
        const val ME = "me"
        const val QUICK_MEETING = "quick_meeting"
        const val JOIN_MEETING = "join_meeting"
        const val SCHEDULED_MEETING = "scheduled_meeting"
        const val MEETING_DETAILS = "meeting_details"
        const val MEETING_CHAT = "meeting_chat"
        const val ADD_FRIENDS = "add_friends"
        const val FRIENDS_DETAILS = "friends_details"
        const val PERSONAL_INFORMATION = "personal_information"
        const val PERSONAL_MEETING_ROOM = "personal_meeting_room"
        const val RECORD = "record"
    }
}
