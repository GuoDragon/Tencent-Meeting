package com.appsim.tencent_meeting_sim.presentation.home

import com.appsim.tencent_meeting_sim.presentation.home.HomeContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.MeetingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePresenter(
    private val dataRepository: DataRepository
) : HomeContract.Presenter {
    
    private var view: HomeContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun attachView(view: HomeContract.View) {
        this.view = view
    }
    
    override fun detachView() {
        this.view = null
    }
    
    
    override fun loadMeetings() {
        presenterScope.launch {
            try {
                val meetings = withContext(Dispatchers.IO) {
                    dataRepository.getMeetings()
                }

                val activeMeetings = meetings.filter {
                    it.status == MeetingStatus.ONGOING || it.status == MeetingStatus.UPCOMING
                }.take(5)

                if (activeMeetings.isNotEmpty()) {
                    view?.showMeetings(activeMeetings)
                } else {
                    view?.showEmptyMeetings()
                }
            } catch (e: Exception) {
                view?.showError("加载会议信息失败: ${e.message}")
            }
        }
    }

    override fun loadCurrentUser() {
        presenterScope.launch {
            try {
                val users = withContext(Dispatchers.IO) {
                    dataRepository.getUsers()
                }
                // 获取刘承龙的用户信息（user001）
                val currentUser = users.firstOrNull { it.userId == "user001" }
                currentUser?.let {
                    view?.showUserInfo(it)
                }
            } catch (e: Exception) {
                view?.showError("加载用户信息失败: ${e.message}")
            }
        }
    }

    override fun onJoinMeetingClicked() {
        view?.navigateToJoinMeeting()
    }

    override fun onQuickMeetingClicked() {
        view?.navigateToQuickMeeting()
    }

    override fun onScheduledMeetingClicked() {
        view?.navigateToScheduledMeeting()
    }

    override fun onHistoryMeetingsClicked() {
        presenterScope.launch {
            try {
                val meetings = withContext(Dispatchers.IO) {
                    dataRepository.getMeetings()
                }

                val historyMeetings = meetings.filter {
                    it.status == MeetingStatus.ENDED
                }

                view?.showHistoryMeetings(historyMeetings)
            } catch (e: Exception) {
                view?.showError("加载历史会议失败: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        detachView()
    }
}