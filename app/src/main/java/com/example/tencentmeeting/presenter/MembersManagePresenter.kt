package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.MembersManageContract
import com.example.tencentmeeting.data.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MembersManagePresenter(
    private val dataRepository: DataRepository
) : MembersManageContract.Presenter {

    private var view: MembersManageContract.View? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun attachView(view: MembersManageContract.View) {
        this.view = view
    }

    override fun onDestroy() {
        presenterJob.cancel()
        view = null
    }

    override fun loadMembers() {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 获取会议成员列表（当前只显示主持人自己）
                val users = dataRepository.getUsers()
                // 只取第一个用户（刘承龙）作为主持人
                view?.showMembers(users.take(1))

            } catch (e: Exception) {
                view?.showError("加载成员失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun muteAll() {
        presenterScope.launch {
            try {
                // 模拟全员静音操作
                view?.showMuteAllSuccess()
            } catch (e: Exception) {
                view?.showError("全员静音失败: ${e.message}")
            }
        }
    }

    override fun unmuteAll() {
        presenterScope.launch {
            try {
                // 模拟解除全员静音操作
                view?.showUnmuteAllSuccess()
            } catch (e: Exception) {
                view?.showError("解除全员静音失败: ${e.message}")
            }
        }
    }

    override fun inviteMember() {
        presenterScope.launch {
            try {
                // 模拟邀请成员操作
                view?.showError("邀请成员功能")
            } catch (e: Exception) {
                view?.showError("邀请失败: ${e.message}")
            }
        }
    }

    override fun searchMember(query: String) {
        presenterScope.launch {
            try {
                // 模拟搜索成员
                if (query.isBlank()) {
                    loadMembers()
                } else {
                    val users = dataRepository.getUsers()
                    val filtered = users.filter { it.username.contains(query) }
                    view?.showMembers(filtered)
                }
            } catch (e: Exception) {
                view?.showError("搜索失败: ${e.message}")
            }
        }
    }
}
