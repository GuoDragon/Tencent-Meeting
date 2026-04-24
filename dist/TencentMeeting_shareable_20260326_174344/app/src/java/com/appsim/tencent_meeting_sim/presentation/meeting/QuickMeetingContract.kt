package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.data.model.User

interface QuickMeetingContract {
    interface View {
        fun showUserInfo(user: User)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showStartMeetingSuccess(meetingId: String, micEnabled: Boolean, videoEnabled: Boolean, speakerEnabled: Boolean)
    }

    interface Presenter {
        fun attachView(view: View)
        fun onDestroy()
        fun loadUserInfo()
        fun startQuickMeeting(
            videoEnabled: Boolean,
            usePersonalMeetingId: Boolean,
            micEnabled: Boolean,
            speakerEnabled: Boolean
        )
    }
}
