package com.appsim.tencent_meeting_sim.presentation.me

import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.data.model.User

interface MeContract {
    
    interface View {
        fun showUserInfo(user: User)
        fun showHistoryMeetings(meetings: List<Meeting>)
        fun showEmptyHistory()
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
    }
    
    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadUserInfo()
        fun loadHistoryMeetings()
        fun onDestroy()
    }
}