package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.ShareScreenInputContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository

class ShareScreenInputPresenter(
    private val dataRepository: DataRepository
) : ShareScreenInputContract.Presenter {

    private var view: ShareScreenInputContract.View? = null

    override fun attachView(view: ShareScreenInputContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun onMeetingIdChanged(meetingId: String) {
        // 会议号改变时不需要特殊处理，UI层会自动更新按钮状态
    }

    override fun onStartShareClicked(meetingId: String) {
        if (meetingId.isBlank()) {
            view?.showError("请输入会议号")
            return
        }

        // 直接导航到会议详情页，会议详情页会自动开启屏幕共享
        view?.navigateToMeetingDetails(meetingId)
    }

    override fun onDestroy() {
        detachView()
    }
}
