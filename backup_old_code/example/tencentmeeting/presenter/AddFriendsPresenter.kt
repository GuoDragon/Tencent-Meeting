package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.AddFriendsContract
import com.example.tencentmeeting.data.DataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddFriendsPresenter(
    private val dataRepository: DataRepository
) : AddFriendsContract.Presenter {

    private var view: AddFriendsContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())

    override fun attachView(view: AddFriendsContract.View) {
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

    override fun copyLink(link: String) {
        // 复制链接到剪贴板的逻辑将在View层处理
        view?.showCopySuccess()
    }

    override fun shareToWeChat() {
        // 分享到微信的功能（模拟实现）
        // 实际APP中这里会调用微信SDK
    }

    override fun onDestroy() {
        detachView()
    }
}
