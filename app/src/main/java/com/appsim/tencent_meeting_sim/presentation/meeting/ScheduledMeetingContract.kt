package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.data.model.User

interface ScheduledMeetingContract {

    interface View {
        fun showUsers(users: List<User>)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showSuccess(message: String)
        fun navigateBack()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadUsers()
        fun saveMeeting(
            topic: String,
            startTime: Long,
            duration: Int,
            recurrence: String,
            participantIds: List<String>,
            password: String?
        )
        fun onDestroy()
    }
}
