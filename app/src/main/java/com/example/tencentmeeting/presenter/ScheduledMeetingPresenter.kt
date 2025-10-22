package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.ScheduledMeetingContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.Meeting
import com.example.tencentmeeting.model.MeetingSettings
import com.example.tencentmeeting.model.MeetingStatus
import com.example.tencentmeeting.model.MeetingType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ScheduledMeetingPresenter(
    private val dataRepository: DataRepository
) : ScheduledMeetingContract.Presenter {

    private var view: ScheduledMeetingContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())

    override fun attachView(view: ScheduledMeetingContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadUsers() {
        presenterScope.launch {
            try {
                view?.showLoading()
                val users = withContext(Dispatchers.IO) {
                    dataRepository.getUsers()
                }
                view?.hideLoading()
                view?.showUsers(users)
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError("加载用户列表失败: ${e.message}")
            }
        }
    }

    override fun saveMeeting(
        topic: String,
        startTime: Long,
        duration: Int,
        recurrence: String,
        participantIds: List<String>,
        password: String?
    ) {
        presenterScope.launch {
            try {
                // 数据验证
                if (topic.isBlank()) {
                    view?.showError("请输入会议主题")
                    return@launch
                }

                if (startTime <= System.currentTimeMillis()) {
                    view?.showError("会议开始时间必须晚于当前时间")
                    return@launch
                }

                if (password != null && password.length != 6) {
                    view?.showError("会议密码必须为6位数字")
                    return@launch
                }

                view?.showLoading()

                // 计算结束时间（开始时间 + 会议时长）
                val endTime = startTime + (duration * 60 * 1000)

                // 生成会议ID
                val meetingId = "meeting_${UUID.randomUUID().toString().substring(0, 8)}"

                // 创建会议对象
                val meeting = Meeting(
                    meetingId = meetingId,
                    topic = topic,
                    password = password,
                    hostId = "user001", // 默认主持人，实际项目中应该使用当前登录用户
                    startTime = startTime,
                    endTime = endTime,
                    status = MeetingStatus.UPCOMING,
                    meetingType = MeetingType.SCHEDULED,
                    participantIds = participantIds,
                    settings = MeetingSettings()
                )

                // 保存会议
                withContext(Dispatchers.IO) {
                    dataRepository.saveMeeting(meeting)
                }

                view?.hideLoading()
                view?.showSuccess("会议预定成功")
                view?.navigateBack()
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError("保存会议失败: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        detachView()
    }
}
