package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.QuickMeetingContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Meeting
import com.appsim.tencent_meeting_sim.data.model.MeetingParticipant
import com.appsim.tencent_meeting_sim.data.model.MeetingSettings
import com.appsim.tencent_meeting_sim.data.model.MeetingStatus
import com.appsim.tencent_meeting_sim.data.model.MeetingType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID

class QuickMeetingPresenter(
    private val dataRepository: DataRepository
) : QuickMeetingContract.Presenter {

    private var view: QuickMeetingContract.View? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun attachView(view: QuickMeetingContract.View) {
        this.view = view
    }

    override fun onDestroy() {
        presenterJob.cancel()
        view = null
    }

    override fun loadUserInfo() {
        presenterScope.launch {
            try {
                view?.showLoading()
                val users = dataRepository.getUsers()
                if (users.isNotEmpty()) {
                    view?.showUserInfo(users.first())
                }
            } catch (e: Exception) {
                view?.showError("加载用户信息失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun startQuickMeeting(
        videoEnabled: Boolean,
        usePersonalMeetingId: Boolean,
        micEnabled: Boolean,
        speakerEnabled: Boolean
    ) {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 创建快速会议
                // 生成唯一的会议ID（用于数据库）
                val actualMeetingId = "meeting_${UUID.randomUUID().toString().substring(0, 8)}"

                // 创建Meeting对象
                val currentTime = System.currentTimeMillis()
                val meeting = Meeting(
                    meetingId = actualMeetingId,
                    topic = "快速会议",
                    password = null,
                    hostId = "user001", // 默认主持人
                    startTime = currentTime,
                    endTime = null, // 会议进行中，endTime为null
                    status = MeetingStatus.ONGOING,
                    meetingType = MeetingType.INSTANT,
                    participantIds = listOf("user001"),
                    settings = MeetingSettings()
                )

                // 保存会议到数据库
                dataRepository.saveMeeting(meeting)
                dataRepository.saveMeetingsToFile()

                // 创建主持人的参会人记录
                val hostParticipant = MeetingParticipant(
                    userId = "user001",  // 主持人ID
                    meetingId = actualMeetingId,
                    isMuted = !micEnabled,
                    isCameraOn = videoEnabled,
                    isHandRaised = false,
                    handRaisedTime = null,
                    isSharingScreen = false,
                    joinTime = currentTime
                )
                dataRepository.addOrUpdateParticipant(hostParticipant)

                // 使用实际的会议ID导航到会议详情页
                view?.showStartMeetingSuccess(actualMeetingId, micEnabled, videoEnabled, speakerEnabled)
            } catch (e: Exception) {
                view?.showError("启动会议失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    private fun generateRandomMeetingId(): String {
        val random = (100000000..999999999).random()
        return random.toString().chunked(3).joinToString("-")
    }
}
