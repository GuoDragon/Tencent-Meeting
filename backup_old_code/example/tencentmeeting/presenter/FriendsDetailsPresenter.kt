package com.example.tencentmeeting.presenter

import com.example.tencentmeeting.contract.FriendsDetailsContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FriendsDetailsPresenter(
    private val dataRepository: DataRepository
) : FriendsDetailsContract.Presenter {

    private var view: FriendsDetailsContract.View? = null
    private val presenterScope = CoroutineScope(Dispatchers.Main + Job())

    override fun attachView(view: FriendsDetailsContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadFriendInfo(friend: User) {
        presenterScope.launch {
            try {
                view?.showLoading()
                // 直接显示传入的好友信息
                view?.showFriendInfo(friend)
            } catch (e: Exception) {
                view?.showError("加载好友信息失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun callFriend(friend: User) {
        // 模拟呼叫功能
        presenterScope.launch {
            try {
                // 在实际应用中，这里会调用系统电话或会议呼叫功能
                view?.showCallSuccess(friend.username)
            } catch (e: Exception) {
                view?.showError("呼叫失败: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        detachView()
    }
}
