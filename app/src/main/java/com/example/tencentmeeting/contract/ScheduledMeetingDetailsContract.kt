package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.Meeting

interface ScheduledMeetingDetailsContract {

    interface View {
        fun showMeetingDetails(meeting: Meeting)
        fun showError(message: String)
        fun navigateToMeetingDetails(meetingId: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadMeetingDetails(meetingId: String)
        fun onEnterMeetingClicked()
        fun onDestroy()
    }
}
