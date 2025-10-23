package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.MeetingDetailsContract
import com.example.tencentmeeting.data.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MeetingDetailsPresenter(
    private val dataRepository: DataRepository
) : MeetingDetailsContract.Presenter {

    private var view: MeetingDetailsContract.View? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    private var micEnabled = false
    private var videoEnabled = false
    private var speakerEnabled = true
    private var isScreenSharing = false

    private var durationJob: Job? = null
    private var durationSeconds = 0

    override fun attachView(view: MeetingDetailsContract.View) {
        this.view = view
    }

    override fun onDestroy() {
        durationJob?.cancel()
        presenterJob.cancel()
        view = null
    }

    override fun loadMeetingDetails(meetingId: String) {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 加载会议信息
                val meetings = dataRepository.getMeetings()
                val meeting = meetings.find { it.meetingId == meetingId }

                if (meeting != null) {
                    view?.showMeetingInfo(meeting.topic, meeting.meetingId)
                } else {
                    view?.showMeetingInfo("快速会议", meetingId)
                }

                // 加载参会人列表
                val users = dataRepository.getUsers()
                view?.showParticipants(users.take(1)) // 暂时只显示当前用户

                // 启动会议计时
                startMeetingDuration()

            } catch (e: Exception) {
                view?.showError("加载会议信息失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    private fun startMeetingDuration() {
        durationJob = presenterScope.launch {
            while (true) {
                delay(1000)
                durationSeconds++
                val hours = durationSeconds / 3600
                val minutes = (durationSeconds % 3600) / 60
                val seconds = durationSeconds % 60
                val duration = String.format("%02d:%02d", minutes, seconds)
                view?.showMeetingDuration(duration)
            }
        }
    }

    override fun toggleMic() {
        micEnabled = !micEnabled
        view?.updateMicStatus(micEnabled)
    }

    override fun toggleVideo() {
        videoEnabled = !videoEnabled
        view?.updateVideoStatus(videoEnabled)
    }

    override fun toggleSpeaker() {
        speakerEnabled = !speakerEnabled
        view?.updateSpeakerStatus(speakerEnabled)
    }

    override fun shareScreen() {
        // 切换屏幕共享状态
        isScreenSharing = !isScreenSharing
        view?.updateScreenShareStatus(isScreenSharing)
    }

    override fun manageMember() {
        // 模拟管理成员功能
        presenterScope.launch {
            view?.showError("管理成员功能")
        }
    }

    override fun endMeeting() {
        durationJob?.cancel()
        view?.navigateBack()
    }

    override fun sendDanmu(message: String) {
        // 模拟发送弹幕功能
        presenterScope.launch {
            if (message.isNotBlank()) {
                view?.showError("已发送: $message")
            }
        }
    }
}
