package com.example.tencentmeeting.presenter

import android.content.Context
import com.example.tencentmeeting.contract.PersonalMeetingRoomContract
import com.example.tencentmeeting.data.DataRepository
import com.example.tencentmeeting.model.PersonalMeetingRoom

/**
 * 个人会议室页面的Presenter实现
 * Personal Meeting Room page Presenter implementation
 */
class PersonalMeetingRoomPresenter(
    private val context: Context
) : PersonalMeetingRoomContract.Presenter {

    private var view: PersonalMeetingRoomContract.View? = null
    private val repository = DataRepository.getInstance(context)
    private var currentRoomInfo: PersonalMeetingRoom? = null

    override fun attachView(view: PersonalMeetingRoomContract.View) {
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    override fun loadMeetingRoomInfo(userId: String) {
        try {
            // 加载个人会议室信息
            val roomInfo = repository.getPersonalMeetingRoom(userId)
            if (roomInfo != null) {
                currentRoomInfo = roomInfo

                // 加载用户信息
                val users = repository.getUsers()
                val user = users.find { it.userId == userId }

                if (user != null) {
                    view?.showMeetingRoomInfo(roomInfo, user)
                } else {
                    view?.showError("用户信息不存在")
                }
            } else {
                view?.showError("个人会议室信息不存在")
            }
        } catch (e: Exception) {
            view?.showError("加载会议室信息失败：${e.message}")
        }
    }

    override fun updatePassword(password: String?, enabled: Boolean) {
        currentRoomInfo?.let { room ->
            room.password = password
            room.enablePassword = enabled
            repository.savePersonalMeetingRoom(room)
            view?.updateSettings(room)
        }
    }

    override fun updateWaitingRoom(enabled: Boolean) {
        currentRoomInfo?.let { room ->
            room.enableWaitingRoom = enabled
            repository.savePersonalMeetingRoom(room)
            view?.updateSettings(room)
        }
    }

    override fun updateAllowBeforeHost(allowed: Boolean) {
        currentRoomInfo?.let { room ->
            room.allowBeforeHost = allowed
            repository.savePersonalMeetingRoom(room)
            view?.updateSettings(room)
        }
    }

    override fun updateWatermark(enabled: Boolean) {
        currentRoomInfo?.let { room ->
            room.enableWatermark = enabled
            repository.savePersonalMeetingRoom(room)
            view?.updateSettings(room)
        }
    }

    override fun updateMuteOnEntry(rule: String) {
        currentRoomInfo?.let { room ->
            room.muteOnEntry = rule
            repository.savePersonalMeetingRoom(room)
            view?.updateSettings(room)
        }
    }

    override fun updateMultiDevice(allowed: Boolean) {
        currentRoomInfo?.let { room ->
            room.allowMultiDevice = allowed
            repository.savePersonalMeetingRoom(room)
            view?.updateSettings(room)
        }
    }

    override fun onDestroy() {
        detachView()
    }
}
