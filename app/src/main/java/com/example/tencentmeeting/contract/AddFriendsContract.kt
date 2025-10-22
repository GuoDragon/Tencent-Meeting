package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.User

interface AddFriendsContract {

    interface View {
        fun showUserInfo(user: User)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showCopySuccess()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadUserInfo()
        fun copyLink(link: String)
        fun shareToWeChat()
        fun onDestroy()
    }
}
