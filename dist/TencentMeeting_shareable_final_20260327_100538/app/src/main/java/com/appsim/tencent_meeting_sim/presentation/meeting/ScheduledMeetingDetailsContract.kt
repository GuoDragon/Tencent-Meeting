package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.data.model.Meeting

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
