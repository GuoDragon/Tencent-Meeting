package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.HistoryMeetingsContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.MeetingStatus
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

                // 过滤出已结束的会议，并按开始时间倒序排列
                val historyMeetings = meetings
                    .filter { it.status == MeetingStatus.ENDED }
                    .sortedByDescending { it.startTime }

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
