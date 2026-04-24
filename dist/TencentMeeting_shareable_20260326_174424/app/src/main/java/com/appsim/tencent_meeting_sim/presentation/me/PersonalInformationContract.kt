package com.appsim.tencent_meeting_sim.presentation.me

import com.appsim.tencent_meeting_sim.data.model.User

interface PersonalInformationContract {
    interface View {
        fun showUserInfo(user: User)
        fun showSignature(signature: String)
        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadUserInfo(userId: String)
        fun updateSignature(signature: String)
        fun onDestroy()
    }
}
