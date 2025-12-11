package com.appsim.tencent_meeting_sim.presentation.me

import com.appsim.tencent_meeting_sim.presentation.me.PersonalInformationContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository

/**
 * 个人信息页面的Presenter实现
 * Personal Information page Presenter implementation
 */
class PersonalInformationPresenter(
    private val repository: DataRepository
) : PersonalInformationContract.Presenter {

    private var view: PersonalInformationContract.View? = null
    private var currentSignature: String = "点击设置签名,所有人均可查看"

    override fun attachView(view: PersonalInformationContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadUserInfo(userId: String) {
        try {
            val users = repository.getUsers()
            val user = users.find { it.userId == userId }

            if (user != null) {
                view?.showUserInfo(user)
                view?.showSignature(currentSignature)
            } else {
                view?.showError("用户信息不存在")
            }
        } catch (e: Exception) {
            view?.showError("加载用户信息失败：${e.message}")
        }
    }

    override fun updateSignature(signature: String) {
        currentSignature = signature
        view?.showSignature(currentSignature)
    }

    override fun onDestroy() {
        detachView()
    }
}
