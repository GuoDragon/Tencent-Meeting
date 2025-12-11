package com.appsim.tencent_meeting_sim.presentation.me

import com.appsim.tencent_meeting_sim.data.model.PersonalMeetingRoom
import com.appsim.tencent_meeting_sim.data.model.User

/**
 * 个人会议室页面的MVP契约接口
 * Personal Meeting Room page MVP contract
 */
interface PersonalMeetingRoomContract {

    interface View {
        /**
         * 显示会议室信息
         * @param roomInfo 个人会议室信息
         * @param user 用户信息
         */
        fun showMeetingRoomInfo(roomInfo: PersonalMeetingRoom, user: User)

        /**
         * 更新会议室设置
         * @param roomInfo 更新后的会议室信息
         */
        fun updateSettings(roomInfo: PersonalMeetingRoom)

        /**
         * 显示错误信息
         * @param message 错误消息
         */
        fun showError(message: String)
    }

    interface Presenter {
        /**
         * 绑定View
         * @param view View实例
         */
        fun attachView(view: View)

        /**
         * 解绑View
         */
        fun detachView()

        /**
         * 加载会议室信息
         * @param userId 用户ID
         */
        fun loadMeetingRoomInfo(userId: String)

        /**
         * 更新入会密码设置
         * @param password 密码
         * @param enabled 是否启用
         */
        fun updatePassword(password: String?, enabled: Boolean)

        /**
         * 更新等候室设置
         * @param enabled 是否启用
         */
        fun updateWaitingRoom(enabled: Boolean)

        /**
         * 更新允许成员在主持人前入会设置
         * @param allowed 是否允许
         */
        fun updateAllowBeforeHost(allowed: Boolean)

        /**
         * 更新会议水印设置
         * @param enabled 是否启用
         */
        fun updateWatermark(enabled: Boolean)

        /**
         * 更新成员入会时静音设置
         * @param rule 静音规则
         */
        fun updateMuteOnEntry(rule: String)

        /**
         * 更新允许成员多端入会设置
         * @param allowed 是否允许
         */
        fun updateMultiDevice(allowed: Boolean)

        /**
         * 销毁Presenter
         */
        fun onDestroy()
    }
}
