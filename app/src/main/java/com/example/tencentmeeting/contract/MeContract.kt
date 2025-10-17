package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.model.User

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