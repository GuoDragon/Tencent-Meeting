package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.User

interface MeetingDetailsContract {
    interface View {
        fun showMeetingInfo(meetingTopic: String, meetingId: String)
        fun showParticipants(participants: List<User>)
        fun updateMicStatus(enabled: Boolean)
        fun updateVideoStatus(enabled: Boolean)
        fun updateSpeakerStatus(enabled: Boolean)
        fun updateScreenShareStatus(isSharing: Boolean)
        fun showMeetingDuration(duration: String)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun navigateBack()
    }

    interface Presenter {
        fun attachView(view: View)
        fun onDestroy()
        fun loadMeetingDetails(meetingId: String)
        fun toggleMic()
        fun toggleVideo()
        fun toggleSpeaker()
        fun shareScreen()
        fun manageMember()
        fun endMeeting()
        fun sendDanmu(message: String)
    }
}
