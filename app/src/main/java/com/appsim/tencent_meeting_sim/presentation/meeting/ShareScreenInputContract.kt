package com.appsim.tencent_meeting_sim.presentation.meeting

interface ShareScreenInputContract {
    interface View {
        fun showError(message: String)
        fun navigateToMeetingDetails(meetingId: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun onMeetingIdChanged(meetingId: String)
        fun onStartShareClicked(meetingId: String)
        fun onDestroy()
    }
}
