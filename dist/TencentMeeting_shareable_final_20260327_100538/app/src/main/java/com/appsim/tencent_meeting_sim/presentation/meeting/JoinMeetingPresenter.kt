package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.JoinMeetingContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.MeetingParticipant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinMeetingPresenter(
    private val dataRepository: DataRepository
) : JoinMeetingContract.Presenter {

    private var view: JoinMeetingContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())

    override fun attachView(view: JoinMeetingContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadUserInfo() {
        presenterScope.launch {
            try {
                view?.showLoading()
                val users = withContext(Dispatchers.IO) {
                    dataRepository.getUsers()
                }

                if (users.isNotEmpty()) {
                    view?.showUserInfo(users.first())
                } else {
                    view?.showError("无法加载用户信息")
                }
            } catch (e: Exception) {
                view?.showError("加载用户信息失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun joinMeeting(
        meetingId: String,
        password: String?,
        micEnabled: Boolean,
        speakerEnabled: Boolean,
        videoEnabled: Boolean
    ) {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 获取当前用户ID
                val users = withContext(Dispatchers.IO) {
                    dataRepository.getUsers()
                }
                val currentUserId = if (users.isNotEmpty()) users.first().userId else "user001"

                // 创建MeetingParticipant对象
                val participant = MeetingParticipant(
                    userId = currentUserId,
                    meetingId = meetingId,
                    isMuted = !micEnabled,
                    isCameraOn = videoEnabled,
                    isHandRaised = false,
                    handRaisedTime = null,
                    isSharingScreen = false,
                    joinTime = System.currentTimeMillis()
                )

                // 保存参会人员状态
                withContext(Dispatchers.IO) {
                    dataRepository.addOrUpdateParticipant(participant)
                }

                // 模拟加入会议的过程
                // 在实际应用中,这里会调用会议SDK的加入会议API
                kotlinx.coroutines.delay(500)
                view?.showJoinSuccess(meetingId, micEnabled, videoEnabled, speakerEnabled)
            } catch (e: Exception) {
                view?.showError("加入会议失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun onDestroy() {
        detachView()
    }
}
