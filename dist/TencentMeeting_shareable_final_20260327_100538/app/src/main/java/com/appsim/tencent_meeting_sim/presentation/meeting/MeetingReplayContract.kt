package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.data.model.Meeting

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
