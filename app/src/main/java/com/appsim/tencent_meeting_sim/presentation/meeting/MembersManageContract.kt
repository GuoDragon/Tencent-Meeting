package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.data.model.User

interface MembersManageContract {
    interface View {
        fun showMembers(members: List<User>)
        fun updateMemberMicStatus(userId: String, enabled: Boolean)
        fun updateMemberVideoStatus(userId: String, enabled: Boolean)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showMuteAllSuccess()
        fun showUnmuteAllSuccess()
        // 新增邀请相关方法
        fun showInviteDialog(availableContacts: List<User>)
        fun hideInviteDialog()
        fun showInviteSuccess(invitedCount: Int)
        fun showInviteFailed(message: String)
        fun updateInvitedMembers(users: List<User>)
    }

    interface Presenter {
        fun attachView(view: View)
        fun onDestroy()
        fun loadMembers()
        fun muteAll()
        fun unmuteAll()
        fun inviteMember()
        fun searchMember(query: String)
        // 新增邀请相关方法
        fun loadAvailableContacts()
        fun sendInvitations(selectedUserIds: List<String>)
        fun loadInvitedMembers(meetingId: String)
    }
}
