package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.data.model.Meeting

interface HistoryMeetingsContract {

    interface View {
        fun showMeetings(meetings: List<Meeting>)
        fun showEmptyState()
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun navigateToMeetingReplay(meetingId: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadHistoryMeetings()
        fun onMeetingClicked(meetingId: String)
        fun onDestroy()
    }
}
