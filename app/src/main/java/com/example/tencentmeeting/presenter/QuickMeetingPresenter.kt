package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.QuickMeetingContract
import com.example.tencentmeeting.data.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

                // 这里可以保存会议设置到内存中
                // 例如保存麦克风、摄像头、扬声器的状态

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
