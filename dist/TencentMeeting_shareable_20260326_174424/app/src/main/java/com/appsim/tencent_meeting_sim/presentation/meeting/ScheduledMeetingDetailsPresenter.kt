package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.ScheduledMeetingDetailsContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduledMeetingDetailsPresenter(
    private val dataRepository: DataRepository
) : ScheduledMeetingDetailsContract.Presenter {

    private var view: ScheduledMeetingDetailsContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())
    private var currentMeetingId: String = ""

    override fun attachView(view: ScheduledMeetingDetailsContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadMeetingDetails(meetingId: String) {
        currentMeetingId = meetingId
        presenterScope.launch {
            try {
                val meetings = withContext(Dispatchers.IO) {
                    dataRepository.getMeetings()
                }

                val meeting = meetings.firstOrNull { it.meetingId == meetingId }
                if (meeting != null) {
                    view?.showMeetingDetails(meeting)
                } else {
                    view?.showError("未找到会议信息")
                }
            } catch (e: Exception) {
                view?.showError("加载会议信息失败: ${e.message}")
            }
        }
    }

    override fun onEnterMeetingClicked() {
        view?.navigateToMeetingDetails(currentMeetingId)
    }

    override fun onDestroy() {
        detachView()
    }
}
