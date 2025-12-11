package com.appsim.tencent_meeting_sim.presentation.meeting

import com.appsim.tencent_meeting_sim.presentation.meeting.MembersManageContract
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.data.model.InvitationStatus
import com.appsim.tencent_meeting_sim.data.model.MeetingInvitation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.UUID

class MembersManagePresenter(
    private val dataRepository: DataRepository
) : MembersManageContract.Presenter {

    private var view: MembersManageContract.View? = null
    private val presenterJob = Job()
    private val presenterScope = CoroutineScope(Dispatchers.Main + presenterJob)

    override fun attachView(view: MembersManageContract.View) {
        this.view = view
    }

    override fun onDestroy() {
        presenterJob.cancel()
        view = null
    }

    override fun loadMembers() {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 获取会议成员列表（当前用户和前5位好友，共6人）
                val users = dataRepository.getUsers()
                view?.showMembers(users.take(6))

            } catch (e: Exception) {
                view?.showError("加载成员失败: ${e.message}")
            } finally {
                view?.hideLoading()
            }
        }
    }

    override fun muteAll() {
        presenterScope.launch {
            try {
                val participants = dataRepository.getMeetingParticipants()

                participants.forEach { participant ->
                    dataRepository.addOrUpdateParticipant(
                        participant.copy(isMuted = true)
                    )
                }

                dataRepository.saveMeetingParticipantsToFile(participants.map { it.copy(isMuted = true) })
                view?.showMuteAllSuccess()
            } catch (e: Exception) {
                view?.showError("全员静音失败: ${e.message}")
            }
        }
    }

    override fun unmuteAll() {
        presenterScope.launch {
            try {
                val participants = dataRepository.getMeetingParticipants()

                participants.forEach { participant ->
                    dataRepository.addOrUpdateParticipant(
                        participant.copy(isMuted = false)
                    )
                }

                dataRepository.saveMeetingParticipantsToFile(participants.map { it.copy(isMuted = false) })
                view?.showUnmuteAllSuccess()
            } catch (e: Exception) {
                view?.showError("解除全员静音失败: ${e.message}")
            }
        }
    }

    override fun inviteMember() {
        presenterScope.launch {
            try {
                view?.showLoading()

                // 获取可邀请的联系人
                val availableContacts = dataRepository.getAvailableUsersToInvite("meeting001", "user001")

                if (availableContacts.isEmpty()) {
                    view?.showError("暂无可邀请的联系人")
                    view?.hideLoading()
                } else {
                    view?.hideLoading()
                    view?.showInviteDialog(availableContacts)
                }
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showError("加载联系人失败: ${e.message}")
            }
        }
    }

    override fun loadAvailableContacts() {
        presenterScope.launch {
            try {
                view?.showLoading()
                val availableContacts = dataRepository.getAvailableUsersToInvite("meeting001", "user001")
                view?.hideLoading()
                view?.showInviteDialog(availableContacts)
            } catch (e: Exception) {
                view?.hideLoading()
                view?.showInviteFailed("获取联系人失败: ${e.message}")
            }
        }
    }

    override fun sendInvitations(selectedUserIds: List<String>) {
        presenterScope.launch {
            try {
                val currentTime = System.currentTimeMillis()

                // 为每个选中的用户创建邀请
                selectedUserIds.forEach { userId ->
                    val invitation = MeetingInvitation(
                        invitationId = UUID.randomUUID().toString(),
                        meetingId = "meeting001",
                        inviterId = "user001", // 主持人ID
                        inviteeId = userId,
                        status = InvitationStatus.PENDING,
                        invitedTime = currentTime
                    )
                    dataRepository.addInvitation(invitation)
                }

                // 保存邀请到文件
                dataRepository.saveInvitationsToFile()

                view?.showInviteSuccess(selectedUserIds.size)
            } catch (e: Exception) {
                view?.showInviteFailed("发送邀请失败: ${e.message}")
            }
        }
    }

    override fun searchMember(query: String) {
        presenterScope.launch {
            try {
                // 模拟搜索成员
                if (query.isBlank()) {
                    loadMembers()
                } else {
                    val users = dataRepository.getUsers()
                    val filtered = users.filter { it.username.contains(query) }
                    view?.showMembers(filtered)
                }
            } catch (e: Exception) {
                view?.showError("搜索失败: ${e.message}")
            }
        }
    }

    override fun loadInvitedMembers(meetingId: String) {
        presenterScope.launch {
            try {
                val invitations = dataRepository.getInvitationsByMeetingId(meetingId)
                    .filter { it.status == InvitationStatus.PENDING }

                val allUsers = dataRepository.getUsers()
                val invitedUsers = allUsers.filter { user ->
                    invitations.any { it.inviteeId == user.userId }
                }

                view?.updateInvitedMembers(invitedUsers)
            } catch (e: Exception) {
                view?.showError("加载已邀请成员失败: ${e.message}")
            }
        }
    }
}
