package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.model.User

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
    }
    
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadMeetings()
        fun onJoinMeetingClicked()
        fun onQuickMeetingClicked()
        fun onScheduledMeetingClicked()
        fun onDestroy()
    }
}