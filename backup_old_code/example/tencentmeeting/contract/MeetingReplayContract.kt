package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.Meeting

interface MeetingReplayContract {
    interface View {
        fun showMeetingInfo(meeting: Meeting)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun loadMeetingInfo(meetingId: String)
        fun onDestroy()
    }
}
