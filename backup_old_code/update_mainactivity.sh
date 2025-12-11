#!/bin/bash
FILE="D:/android_template/Tencent-Meeting/app/src/main/java/com/appsim/tencent_meeting_sim/MainActivity.kt"

# 更新package声明
sed -i 's/package com\.example\.tencentmeeting/package com.appsim.tencent_meeting_sim/' "$FILE"

# 更新theme import
sed -i 's/import com\.example\.tencentmeeting\.ui\.theme\./import com.appsim.tencent_meeting_sim.ui.theme./' "$FILE"

# 更新view imports -> presentation (根据模块分类)
sed -i 's/import com\.example\.tencentmeeting\.view\.HomePage/import com.appsim.tencent_meeting_sim.presentation.home.HomeScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.MePage/import com.appsim.tencent_meeting_sim.presentation.me.MeScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.ContactPage/import com.appsim.tencent_meeting_sim.presentation.contact.ContactScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.AddFriendsPage/import com.appsim.tencent_meeting_sim.presentation.contact.AddFriendsScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.FriendsDetailsPage/import com.appsim.tencent_meeting_sim.presentation.contact.FriendsDetailsScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.PersonalMeetingRoomPage/import com.appsim.tencent_meeting_sim.presentation.me.PersonalMeetingRoomScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.PersonalInformationPage/import com.appsim.tencent_meeting_sim.presentation.me.PersonalInformationScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.RecordPage/import com.appsim.tencent_meeting_sim.presentation.me.RecordScreen/' "$FILE"

# Meeting模块的imports
sed -i 's/import com\.example\.tencentmeeting\.view\.ScheduledMeetingPage/import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.JoinMeetingPage/import com.appsim.tencent_meeting_sim.presentation.meeting.JoinMeetingScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.QuickMeetingPage/import com.appsim.tencent_meeting_sim.presentation.meeting.QuickMeetingScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.MeetingDetailsPage/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingDetailsScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.MeetingChatPage/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingChatScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.MeetingReplayPage/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingReplayScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.ScheduledMeetingDetailsPage/import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingDetailsScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.HistoryMeetingsPage/import com.appsim.tencent_meeting_sim.presentation.meeting.HistoryMeetingsScreen/' "$FILE"
sed -i 's/import com\.example\.tencentmeeting\.view\.ShareScreenInputPage/import com.appsim.tencent_meeting_sim.presentation.meeting.ShareScreenInputScreen/' "$FILE"

# 更新model import
sed -i 's/import com\.example\.tencentmeeting\.model\./import com.appsim.tencent_meeting_sim.data.model./' "$FILE"

# 更新data import
sed -i 's/import com\.example\.tencentmeeting\.data\./import com.appsim.tencent_meeting_sim.data.repository./' "$FILE"

# 更新函数调用中的Page -> Screen
sed -i 's/HomePage(/HomeScreen(/' "$FILE"
sed -i 's/MePage(/MeScreen(/' "$FILE"
sed -i 's/ContactPage(/ContactScreen(/' "$FILE"
sed -i 's/AddFriendsPage(/AddFriendsScreen(/' "$FILE"
sed -i 's/FriendsDetailsPage(/FriendsDetailsScreen(/' "$FILE"
sed -i 's/ScheduledMeetingPage(/ScheduledMeetingScreen(/' "$FILE"
sed -i 's/JoinMeetingPage(/JoinMeetingScreen(/' "$FILE"
sed -i 's/QuickMeetingPage(/QuickMeetingScreen(/' "$FILE"
sed -i 's/MeetingDetailsPage(/MeetingDetailsScreen(/' "$FILE"
sed -i 's/MeetingChatPage(/MeetingChatScreen(/' "$FILE"
sed -i 's/MeetingReplayPage(/MeetingReplayScreen(/' "$FILE"
sed -i 's/ScheduledMeetingDetailsPage(/ScheduledMeetingDetailsScreen(/' "$FILE"
sed -i 's/HistoryMeetingsPage(/HistoryMeetingsScreen(/' "$FILE"
sed -i 's/ShareScreenInputPage(/ShareScreenInputScreen(/' "$FILE"
sed -i 's/PersonalMeetingRoomPage(/PersonalMeetingRoomScreen(/' "$FILE"
sed -i 's/PersonalInformationPage(/PersonalInformationScreen(/' "$FILE"
sed -i 's/RecordPage(/RecordScreen(/' "$FILE"

echo "MainActivity更新完成"
