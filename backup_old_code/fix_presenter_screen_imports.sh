#!/bin/bash

BASE_DIR="D:/android_template/Tencent-Meeting/app/src/main/java/com/appsim/tencent_meeting_sim/presentation"

echo "=== 修复Contact模块 ==="
find "$BASE_DIR/contact" -name "*.kt" -type f | while read file; do
  # 修复Contract imports - 添加 .contact
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.AddFriendsContract/import com.appsim.tencent_meeting_sim.presentation.contact.AddFriendsContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.ContactContract/import com.appsim.tencent_meeting_sim.presentation.contact.ContactContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.FriendsDetailsContract/import com.appsim.tencent_meeting_sim.presentation.contact.FriendsDetailsContract/g' "$file"

  # 修复Presenter imports - 添加 .contact
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.AddFriendsPresenter/import com.appsim.tencent_meeting_sim.presentation.contact.AddFriendsPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.ContactPresenter/import com.appsim.tencent_meeting_sim.presentation.contact.ContactPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.FriendsDetailsPresenter/import com.appsim.tencent_meeting_sim.presentation.contact.FriendsDetailsPresenter/g' "$file"

  echo "Fixed: $file"
done

echo "=== 修复Home模块 ==="
find "$BASE_DIR/home" -name "*.kt" -type f | while read file; do
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.HomeContract/import com.appsim.tencent_meeting_sim.presentation.home.HomeContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.HomePresenter/import com.appsim.tencent_meeting_sim.presentation.home.HomePresenter/g' "$file"
  echo "Fixed: $file"
done

echo "=== 修复Me模块 ==="
find "$BASE_DIR/me" -name "*.kt" -type f | while read file; do
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MeContract/import com.appsim.tencent_meeting_sim.presentation.me.MeContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MePresenter/import com.appsim.tencent_meeting_sim.presentation.me.MePresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.PersonalInformationContract/import com.appsim.tencent_meeting_sim.presentation.me.PersonalInformationContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.PersonalInformationPresenter/import com.appsim.tencent_meeting_sim.presentation.me.PersonalInformationPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.PersonalMeetingRoomContract/import com.appsim.tencent_meeting_sim.presentation.me.PersonalMeetingRoomContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.PersonalMeetingRoomPresenter/import com.appsim.tencent_meeting_sim.presentation.me.PersonalMeetingRoomPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.RecordContract/import com.appsim.tencent_meeting_sim.presentation.me.RecordContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.RecordPresenter/import com.appsim.tencent_meeting_sim.presentation.me.RecordPresenter/g' "$file"
  echo "Fixed: $file"
done

echo "=== 修复Meeting模块 ==="
find "$BASE_DIR/meeting" -name "*.kt" -type f | while read file; do
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.JoinMeetingContract/import com.appsim.tencent_meeting_sim.presentation.meeting.JoinMeetingContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.JoinMeetingPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.JoinMeetingPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MeetingDetailsContract/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingDetailsContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MeetingDetailsPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingDetailsPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MeetingChatContract/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingChatContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MeetingChatPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingChatPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MembersManageContract/import com.appsim.tencent_meeting_sim.presentation.meeting.MembersManageContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MembersManagePresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.MembersManagePresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.QuickMeetingContract/import com.appsim.tencent_meeting_sim.presentation.meeting.QuickMeetingContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.QuickMeetingPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.QuickMeetingPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.HistoryMeetingsContract/import com.appsim.tencent_meeting_sim.presentation.meeting.HistoryMeetingsContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.HistoryMeetingsPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.HistoryMeetingsPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MeetingReplayContract/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingReplayContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.MeetingReplayPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingReplayPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.ScheduledMeetingContract/import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.ScheduledMeetingPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingPresenter/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.ScheduledMeetingDetailsContract/import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingDetailsContract/g' "$file"
  sed -i 's/import com\.appsim\.tencent_meeting_sim\.presentation\.ScheduledMeetingDetailsPresenter/import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingDetailsPresenter/g' "$file"
  echo "Fixed: $file"
done

echo "=== 所有Presenter和Screen的Contract/Presenter imports已修复! ==="
