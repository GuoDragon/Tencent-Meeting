package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.User

interface JoinMeetingContract {

    interface View {
        fun showUserInfo(user: User)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showJoinSuccess(meetingId: String, micEnabled: Boolean, videoEnabled: Boolean, speakerEnabled: Boolean)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadUserInfo()
        fun joinMeeting(
            meetingId: String,
            password: String?,
            micEnabled: Boolean,
            speakerEnabled: Boolean,
            videoEnabled: Boolean
        )
        fun onDestroy()
    }
}
