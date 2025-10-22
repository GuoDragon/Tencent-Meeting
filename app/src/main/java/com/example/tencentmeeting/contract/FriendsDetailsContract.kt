package com.example.tencentmeeting.contract

import com.example.tencentmeeting.model.User

interface FriendsDetailsContract {

    interface View {
        fun showFriendInfo(user: User)
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showCallSuccess(friendName: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadFriendInfo(friend: User)
        fun callFriend(friend: User)
        fun onDestroy()
    }
}
