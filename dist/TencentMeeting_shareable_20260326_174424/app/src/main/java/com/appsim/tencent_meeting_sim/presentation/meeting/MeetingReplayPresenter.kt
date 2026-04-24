package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingReplayContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MeetingReplayPresenter(
    private val dataRepository: DataRepository
) : MeetingReplayContract.Presenter {

    private var view: MeetingReplayContract.View? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun attachView(view: MeetingReplayContract.View) {
        this.view = view
    }

    override fun loadMeetingInfo(meetingId: String) {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 从数据仓库获取会议信息
                val allMeetings = dataRepository.getMeetings()
                val meeting = allMeetings.find { it.meetingId == meetingId }

                if (meeting != null) {
                    view?.showMeetingInfo(meeting)
                } else {
                    view?.showError("未找到会议记录")
                }
            } catch (e: Exception) {
                view?.showError("加载会议信息失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun onDestroy() {
        presenterJob.cancel()
        view = null
    }
}
