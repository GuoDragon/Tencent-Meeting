package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.User

interface QuickMeetingContract {
    interface View {
        fun showUserInfo(user: User)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showStartMeetingSuccess(meetingId: String)
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
