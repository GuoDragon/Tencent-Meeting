package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.MeetingDetailsContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.HandRaiseRecord
import com.appsim.tencent_meeting_sim.data.model.MeetingParticipant
import com.appsim.tencent_meeting_sim.data.model.MeetingStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MeetingDetailsPresenter(
    private val dataRepository: DataRepository,
    initialMicEnabled: Boolean = false,
    initialVideoEnabled: Boolean = false,
    initialSpeakerEnabled: Boolean = true
) : MeetingDetailsContract.Presenter {

    private var view: MeetingDetailsContract.View? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    private var micEnabled = initialMicEnabled
    private var videoEnabled = initialVideoEnabled
    private var speakerEnabled = initialSpeakerEnabled
    private var isScreenSharing = false

    private var durationJob: Job? = null
    private var durationSeconds = 0

    // 保存当前会议ID和用户ID，用于数据持久化
    private var currentMeetingId: String = ""
    private var currentUserId: String = "user001"

    override fun attachView(view: MeetingDetailsContract.View) {
        this.view = view
    }

    override fun onDestroy() {
        durationJob?.cancel()
        presenterJob.cancel()
        view = null
    }

    override fun loadMeetingDetails(meetingId: String) {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 保存当前会议ID
                currentMeetingId = meetingId

                // 加载当前用户ID
                val users = dataRepository.getUsers()
                if (users.isNotEmpty()) {
                    currentUserId = users.first().userId
                }

                // 加载会议信息
                val meetings = dataRepository.getMeetings()
                val meeting = meetings.find { it.meetingId == meetingId }

                if (meeting != null) {
                    view?.showMeetingInfo(meeting.topic, meeting.meetingId)
                } else {
                    view?.showMeetingInfo("快速会议", meetingId)
                }

                // 加载参会人列表（包含当前用户和前5位好友，共6人）
                val defaultParticipants = users.take(6)
                view?.showParticipants(defaultParticipants)

                // 启动会议计时
                startMeetingDuration()

            } catch (e: Exception) {
                view?.showError("加载会议信息失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    private fun startMeetingDuration() {
        durationJob = presenterScope.launch {
            while (true) {
                delay(1000)
                durationSeconds++
                val hours = durationSeconds / 3600
                val minutes = (durationSeconds % 3600) / 60
                val seconds = durationSeconds % 60
                val duration = String.format("%02d:%02d", minutes, seconds)
                view?.showMeetingDuration(duration)
            }
        }
    }

    override fun toggleMic() {
        micEnabled = !micEnabled
        view?.updateMicStatus(micEnabled)

        // 保存静音状态到数据库
        updateParticipantStatus(currentMeetingId, currentUserId) { participant ->
            participant.copy(isMuted = !micEnabled)
        }
    }

    override fun toggleVideo() {
        videoEnabled = !videoEnabled
        view?.updateVideoStatus(videoEnabled)

        // 保存摄像头状态到数据库
        updateParticipantStatus(currentMeetingId, currentUserId) { participant ->
            participant.copy(isCameraOn = videoEnabled)
        }
    }

    override fun toggleSpeaker() {
        speakerEnabled = !speakerEnabled
        view?.updateSpeakerStatus(speakerEnabled)

        updateParticipantStatus(currentMeetingId, currentUserId) { participant ->
            participant.copy(isSpeakerOn = speakerEnabled)
        }
    }

    override fun shareScreen() {
        // 切换屏幕共享状态
        isScreenSharing = !isScreenSharing
        view?.updateScreenShareStatus(isScreenSharing)

        // 保存屏幕共享状态到数据库
        updateParticipantStatus(currentMeetingId, currentUserId) { participant ->
            participant.copy(isSharingScreen = isScreenSharing)
        }
    }

    override fun manageMember() {
        // 模拟管理成员功能
        presenterScope.launch {
            view?.showError("管理成员功能")
        }
    }

    override fun endMeeting() {
        presenterScope.launch {
            try {
                val endTime = System.currentTimeMillis()
                android.util.Log.d("MeetingDetails", "结束会议: ID=$currentMeetingId, endTime=$endTime")

                // 更新会议状态为已结束，并设置结束时间
                dataRepository.updateMeeting(currentMeetingId) { meeting ->
                    android.util.Log.d("MeetingDetails", "更新前: ${meeting.topic}, status=${meeting.status}, endTime=${meeting.endTime}")
                    val updated = meeting.copy(
                        status = MeetingStatus.ENDED,
                        endTime = endTime
                    )
                    android.util.Log.d("MeetingDetails", "更新后: ${updated.topic}, status=${updated.status}, endTime=${updated.endTime}")
                    updated
                }

                // 验证更新结果
                val allMeetings = dataRepository.getMeetings()
                val updatedMeeting = allMeetings.find { it.meetingId == currentMeetingId }
                android.util.Log.d("MeetingDetails", "验证更新: ${updatedMeeting?.topic}, status=${updatedMeeting?.status}, endTime=${updatedMeeting?.endTime}")

                durationJob?.cancel()
                view?.navigateBack()
            } catch (e: Exception) {
                android.util.Log.e("MeetingDetails", "结束会议失败", e)
                e.printStackTrace()
                // 即使失败也要返回，避免用户卡住
                durationJob?.cancel()
                view?.navigateBack()
            }
        }
    }

    override fun sendDanmu(message: String) {
        // 模拟发送弹幕功能
        presenterScope.launch {
            if (message.isNotBlank()) {
                view?.showError("已发送: $message")
            }
        }
    }

    override fun raiseHand(meetingId: String, userId: String, userName: String) {
        val recordId = "hr_${System.currentTimeMillis()}"
        val record = HandRaiseRecord(
            recordId = recordId,
            meetingId = meetingId,
            userId = userId,
            userName = userName,
            raiseTime = System.currentTimeMillis(),
            lowerTime = null
        )
        dataRepository.addHandRaiseRecord(record)

        // 同时更新participant的举手状态
        updateParticipantStatus(meetingId, userId) { participant ->
            participant.copy(isHandRaised = true, handRaisedTime = record.raiseTime)
        }
    }

    override fun lowerHand(meetingId: String, userId: String, recordId: String) {
        dataRepository.updateHandRaiseLowerTime(recordId, System.currentTimeMillis())

        // 更新participant的举手状态
        updateParticipantStatus(meetingId, userId) { participant ->
            participant.copy(isHandRaised = false, handRaisedTime = null)
        }
    }

    override fun lockMeeting() {
        dataRepository.updateMeeting(currentMeetingId) { meeting ->
            meeting.copy(isLocked = true)
        }
        view?.showError("会议已锁定，新成员无法加入")
    }

    override fun unlockMeeting() {
        dataRepository.updateMeeting(currentMeetingId) { meeting ->
            meeting.copy(isLocked = false)
        }
        view?.showError("会议已解锁")
    }

    override fun setAllowUnmute(allow: Boolean) {
        dataRepository.updateMeeting(currentMeetingId) { meeting ->
            val newSettings = meeting.settings.copy(allowParticipantUnmute = allow)
            meeting.copy(settings = newSettings)
        }

        val message = if (allow) {
            "已允许参会者自行解除静音"
        } else {
            "已禁止参会者自行解除静音"
        }
        view?.showError(message)
    }

    override fun startRecording() {
        dataRepository.updateMeeting(currentMeetingId) { meeting ->
            meeting.copy(
                isRecording = true,
                recordingStartTime = System.currentTimeMillis()
            )
        }
        view?.showError("会议录制已开始")
    }

    override fun stopRecording() {
        dataRepository.updateMeeting(currentMeetingId) { meeting ->
            meeting.copy(
                isRecording = false,
                recordingStartTime = null
            )
        }
        view?.showError("会议录制已停止")
    }

    /**
     * 更新参会人员状态的辅助方法
     * 修复：如果参会人记录不存在，自动创建一个新的
     */
    private fun updateParticipantStatus(
        meetingId: String,
        userId: String,
        updateFn: (MeetingParticipant) -> MeetingParticipant
    ) {
        val participants = dataRepository.getMeetingParticipants()
        var participant = participants.find { it.meetingId == meetingId && it.userId == userId }

        // 如果参会人记录不存在，创建一个默认的
        if (participant == null) {
            participant = MeetingParticipant(
                userId = userId,
                meetingId = meetingId,
                isMuted = !micEnabled,  // 使用当前状态
                isCameraOn = videoEnabled,
                isHandRaised = false,
                handRaisedTime = null,
                isSharingScreen = false,
                joinTime = System.currentTimeMillis()
            )
        }

        // 应用更新函数
        val updated = updateFn(participant)
        dataRepository.addOrUpdateParticipant(updated)
    }
}
