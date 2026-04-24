package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.data.model.Message

/**
 * 会议聊天页面的MVP Contract
 * 定义View和Presenter之间的交互接口
 */
interface MeetingChatContract {

    /**
     * View接口 - 定义UI层需要实现的方法
     */
    interface View {
        /**
         * 显示消息列表
         * @param messages 消息列表
         */
        fun showMessages(messages: List<Message>)

        /**
         * 添加新消息到列表
         * @param message 新消息
         */
        fun addNewMessage(message: Message)

        /**
         * 显示加载状态
         */
        fun showLoading()

        /**
         * 隐藏加载状态
         */
        fun hideLoading()

        /**
         * 显示错误信息
         * @param message 错误信息
         */
        fun showError(message: String)

        /**
         * 清空输入框
         */
        fun clearInput()

        /**
         * 滚动到最新消息
         */
        fun scrollToLatest()
    }

    /**
     * Presenter接口 - 定义业务逻辑层需要实现的方法
     */
    interface Presenter {
        /**
         * 绑定View
         * @param view View接口实例
         */
        fun attachView(view: View)

        /**
         * 加载会议的聊天消息
         * @param meetingId 会议ID
         */
        fun loadMessages(meetingId: String)

        /**
         * 发送消息
         * @param meetingId 会议ID
         * @param content 消息内容
         */
        fun sendMessage(meetingId: String, content: String)

        /**
         * 销毁Presenter，释放资源
         */
        fun onDestroy()
    }
}
