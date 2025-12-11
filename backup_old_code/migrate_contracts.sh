#!/bin/bash

OLD_BASE="D:/android_template/Tencent-Meeting/app/src/main/java/com/example/tencentmeeting/contract"
NEW_BASE="D:/android_template/Tencent-Meeting/app/src/main/java/com/appsim/tencent_meeting_sim/presentation"

# Home module
cp "$OLD_BASE/HomeContract.kt" "$NEW_BASE/home/"
sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.home/g' "$NEW_BASE/home/HomeContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/home/HomeContract.kt"

# Contact module
cp "$OLD_BASE/ContactContract.kt" "$NEW_BASE/contact/"
cp "$OLD_BASE/AddFriendsContract.kt" "$NEW_BASE/contact/"
cp "$OLD_BASE/FriendsDetailsContract.kt" "$NEW_BASE/contact/"

sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.contact/g' "$NEW_BASE/contact/ContactContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/contact/ContactContract.kt"

sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.contact/g' "$NEW_BASE/contact/AddFriendsContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/contact/AddFriendsContract.kt"

sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.contact/g' "$NEW_BASE/contact/FriendsDetailsContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/contact/FriendsDetailsContract.kt"

# Me module
cp "$OLD_BASE/MeContract.kt" "$NEW_BASE/me/"
cp "$OLD_BASE/PersonalInformationContract.kt" "$NEW_BASE/me/"
cp "$OLD_BASE/PersonalMeetingRoomContract.kt" "$NEW_BASE/me/"
cp "$OLD_BASE/RecordContract.kt" "$NEW_BASE/me/"

sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.me/g' "$NEW_BASE/me/MeContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/me/MeContract.kt"

sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.me/g' "$NEW_BASE/me/PersonalInformationContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/me/PersonalInformationContract.kt"

sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.me/g' "$NEW_BASE/me/PersonalMeetingRoomContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/me/PersonalMeetingRoomContract.kt"

sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.me/g' "$NEW_BASE/me/RecordContract.kt"
sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/me/RecordContract.kt"

# Meeting module
cp "$OLD_BASE/MeetingDetailsContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/JoinMeetingContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/QuickMeetingContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/MeetingChatContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/HistoryMeetingsContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/MeetingReplayContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/ScheduledMeetingContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/ScheduledMeetingDetailsContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/MembersManageContract.kt" "$NEW_BASE/meeting/"
cp "$OLD_BASE/ShareScreenInputContract.kt" "$NEW_BASE/meeting/"

for file in MeetingDetailsContract JoinMeetingContract QuickMeetingContract MeetingChatContract HistoryMeetingsContract MeetingReplayContract ScheduledMeetingContract ScheduledMeetingDetailsContract MembersManageContract ShareScreenInputContract; do
  sed -i 's/package com\.example\.tencentmeeting\.contract/package com.appsim.tencent_meeting_sim.presentation.meeting/g' "$NEW_BASE/meeting/${file}.kt"
  sed -i 's/import com\.example\.tencentmeeting\./import com.appsim.tencent_meeting_sim./g' "$NEW_BASE/meeting/${file}.kt"
done

echo "Contract files migrated successfully!"
