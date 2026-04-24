package com.appsim.tencent_meeting_sim.presentation.me

import com.appsim.tencent_meeting_sim.presentation.me.MeContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.MeetingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MePresenter(
    private val dataRepository: DataRepository
) : MeContract.Presenter {
    
    private var view: MeContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())
    
    override fun attachView(view: MeContract.View) {
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
    
    override fun loadHistoryMeetings() {
        presenterScope.launch {
            try {
                val meetings = withContext(Dispatchers.IO) {
                    dataRepository.getMeetings()
                }
                
                val historyMeetings = meetings.filter { 
                    it.status == MeetingStatus.ENDED 
                }
                
                if (historyMeetings.isNotEmpty()) {
                    view?.showHistoryMeetings(historyMeetings)
                } else {
                    view?.showEmptyHistory()
                }
            } catch (e: Exception) {
                view?.showError("加载历史会议失败: ${e.message}")
            }
        }
    }
    
    override fun onDestroy() {
        detachView()
    }
}