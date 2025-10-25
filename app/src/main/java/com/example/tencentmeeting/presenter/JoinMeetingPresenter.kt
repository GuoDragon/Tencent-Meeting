package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.JoinMeetingContract
import com.example.tencentmeeting.data.DataRepository
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
