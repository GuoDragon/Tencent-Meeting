package com.appsim.tencent_meeting_sim.presentation.home

import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.data.model.User

interface HomeContract {

    interface View {
        fun showMeetings(meetings: List<Meeting>)
        fun showEmptyMeetings()
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun navigateToJoinMeeting()
        fun navigateToQuickMeeting()
        fun navigateToScheduledMeeting()
        fun showUserInfo(user: User)
        fun showHistoryMeetings(meetings: List<Meeting>)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadMeetings()
        fun onJoinMeetingClicked()
        fun onQuickMeetingClicked()
        fun onScheduledMeetingClicked()
        fun loadCurrentUser()
        fun onHistoryMeetingsClicked()
        fun onDestroy()
    }
}