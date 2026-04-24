package com.appsim.tencent_meeting_sim.presentation.me

interface RecordContract {
    interface View {
        fun showStorageInfo(usedMB: Int, totalMB: Int, fileCount: Int)
        fun showEmptyState()
        fun showError(message: String)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun loadStorageInfo()
        fun onDestroy()
    }
}
