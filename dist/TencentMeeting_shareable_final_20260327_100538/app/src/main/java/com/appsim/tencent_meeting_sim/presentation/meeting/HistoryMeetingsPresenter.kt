package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.HistoryMeetingsContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.MeetingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryMeetingsPresenter(
    private val dataRepository: DataRepository
) : HistoryMeetingsContract.Presenter {

    private var view: HistoryMeetingsContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())

    override fun attachView(view: HistoryMeetingsContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadHistoryMeetings() {
        presenterScope.launch {
            view?.showLoading()
            try {
                val meetings = withContext(Dispatchers.IO) {
                    dataRepository.getMeetings()
                }

                // 过滤出已结束的会议，并按结束时间倒序排列（最近结束的在前面）
                val historyMeetings = meetings
                    .filter { it.status == MeetingStatus.ENDED }
                    .sortedByDescending { it.endTime ?: 0L }

                // 调试日志：打印前3个会议的信息
                historyMeetings.take(3).forEachIndexed { index, meeting ->
                    android.util.Log.d("HistoryMeetings",
                        "[$index] ${meeting.topic} - endTime: ${meeting.endTime}, status: ${meeting.status}")
                }

                if (historyMeetings.isNotEmpty()) {
                    view?.showMeetings(historyMeetings)
                } else {
                    view?.showEmptyState()
                }
            } catch (e: Exception) {
                view?.showError("加载历史会议失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun onMeetingClicked(meetingId: String) {
        view?.navigateToMeetingReplay(meetingId)
    }

    override fun onDestroy() {
        detachView()
    }
}
