package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingChatContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * 会议聊天页面的Presenter实现
 * 处理聊天相关的业务逻辑
 */
class MeetingChatPresenter(
    private val dataRepository: DataRepository
) : MeetingChatContract.Presenter {

    private var view: MeetingChatContract.View? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun attachView(view: MeetingChatContract.View) {
        this.view = view
    }

    override fun loadMessages(meetingId: String) {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 模拟网络延迟
                delay(300)

                // 从数据仓库加载消息
                val messages = dataRepository.getMessagesByMeetingId(meetingId)

                view?.hideLoading()
                view?.showMessages(messages)
                view?.scrollToLatest()

            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError("加载消息失败: ${e.message}")
            }
        }
    }

    override fun sendMessage(meetingId: String, content: String) {
        if (content.isBlank()) {
            view?.showError("消息内容不能为空")
            return
        }

        presenterScope.launch {
            try {
                // 创建新消息
                val newMessage = Message(
                    messageId = "msg_${UUID.randomUUID()}",
                    meetingId = meetingId,
                    senderId = "user001", // 当前用户ID（固定为user001）
                    senderName = "我", // 当前用户显示为"我"
                    content = content,
                    timestamp = System.currentTimeMillis()
                )

                // 添加消息到数据仓库
                dataRepository.addMessage(newMessage)

                // 保存消息到文件
                dataRepository.saveMessagesToFile()

                // 通知View更新UI
                view?.addNewMessage(newMessage)
                view?.clearInput()
                view?.scrollToLatest()

            } catch (e: Exception) {
                view?.showError("发送消息失败: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        presenterJob.cancel()
        view = null
    }
}
