package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.QuickMeetingContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.model.MeetingSettings
import com.example.tencentmeeting.model.MeetingStatus
import com.example.tencentmeeting.model.MeetingType
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

                // 模拟创建快速会议
                val meetingId = if (usePersonalMeetingId) {
                    "123-456-789" // 个人会议号
                } else {
                    generateRandomMeetingId()
                }

                // 创建Meeting对象
                val currentTime = System.currentTimeMillis()
                val meeting = Meeting(
                    meetingId = "meeting_${UUID.randomUUID().toString().substring(0, 8)}",
                    topic = "快速会议",
                    password = null,
                    hostId = "user001", // 默认主持人
                    startTime = currentTime,
                    endTime = currentTime + (60 * 60 * 1000), // 默认1小时
                    status = MeetingStatus.ONGOING,
                    meetingType = MeetingType.INSTANT,
                    participantIds = listOf("user001"),
                    settings = MeetingSettings()
                )

                // 保存会议到数据库
                dataRepository.saveMeeting(meeting)
                dataRepository.saveMeetingsToFile()

                view?.showStartMeetingSuccess(meetingId, micEnabled, videoEnabled, speakerEnabled)
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
